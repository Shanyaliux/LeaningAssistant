package com.shanya.leaningassistant.word

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.shanya.leaningassistant.R
import kotlinx.android.synthetic.main.word_add_layout.view.*
import kotlinx.android.synthetic.main.word_fragment.*

class WordFragment : Fragment() {

    companion object {
        fun newInstance() = WordFragment()
    }

    private lateinit var viewModel: WordViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var wordAdapter: WordAdapter
    private lateinit var filtererWords: LiveData<List<Word>>
    private lateinit var allWords: List<Word>
    private var undoAction: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.word_fragment, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        recyclerView = requireActivity().findViewById(R.id.wordsRecycylerView)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        wordAdapter = WordAdapter(requireActivity(),viewModel)

        //设置添加删除动画结束后序号变更
        recyclerView.itemAnimator = object : DefaultItemAnimator() {
            override fun onAnimationFinished(viewHolder: RecyclerView.ViewHolder) {
                super.onAnimationFinished(viewHolder)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (linearLayoutManager != null) {
                    val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
                    val lastPosition = linearLayoutManager.findLastVisibleItemPosition()
                    for (i in firstPosition..lastPosition) {
                        val holder: WordAdapter.WordViewHolder? =
                            recyclerView.findViewHolderForAdapterPosition(i) as WordAdapter.WordViewHolder?
                        holder?.textViewWordNum?.text = (i + 1).toString()
                    }
                }
            }
        }

        recyclerView.adapter = wordAdapter
        recyclerView.addItemDecoration(DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL))

        filtererWords = viewModel.allWords
        filtererWords.observe(viewLifecycleOwner, Observer {

            val temp:Int = wordAdapter.itemCount
            allWords = it
            if (temp != it.size){
                if (temp < it.size && !undoAction){
                    recyclerView.smoothScrollBy(0,-300)
                }
                undoAction = false
                wordAdapter.submitList(it)
            }
        })

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean { //快速拖动会出Bug ，可以出单独选项进行操作

                return false
            }

            //滑动删除
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val wordToDelete = allWords[viewHolder.adapterPosition]
                viewModel.delete(wordToDelete)
                Snackbar.make(
                    requireActivity().findViewById(R.id.wordsRootLayout),
                    "删除了一个词汇",
                    Snackbar.LENGTH_SHORT
                )
                    .setAction("撤销") {
                        undoAction = true
                        viewModel.insert(wordToDelete)
                    }.show()
            }

            //在滑动的时候，画出浅灰色背景和垃圾桶图标，增强删除的视觉效果
            var icon = ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_delete_forever_black_24dp
            )
            var background: Drawable = ColorDrawable(Color.LTGRAY)
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
                val iconLeft: Int
                val iconRight: Int
                val iconTop: Int
                val iconBottom: Int
                val backTop: Int
                val backBottom: Int
                val backLeft: Int
                val backRight: Int
                backTop = itemView.top
                backBottom = itemView.bottom
                iconTop = itemView.top + (itemView.height - icon!!.intrinsicHeight) / 2
                iconBottom = iconTop + icon!!.intrinsicHeight
                when {
                    dX > 0 -> {
                        backLeft = itemView.left
                        backRight = itemView.left + dX.toInt()
                        background.setBounds(backLeft, backTop, backRight, backBottom)
                        iconLeft = itemView.left + iconMargin
                        iconRight = iconLeft + icon!!.intrinsicWidth
                        icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    }
                    dX < 0 -> {
                        backRight = itemView.right
                        backLeft = itemView.right + dX.toInt()
                        background.setBounds(backLeft, backTop, backRight, backBottom)
                        iconRight = itemView.right - iconMargin
                        iconLeft = iconRight - icon!!.intrinsicWidth
                        icon!!.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    }
                    else -> {
                        background.setBounds(0, 0, 0, 0)
                        icon!!.setBounds(0, 0, 0, 0)
                    }
                }
                background.draw(c)
                icon!!.draw(c)
            }
        }).attachToRecyclerView(recyclerView)

        requireActivity().WordAddFloatingActionButton.setOnClickListener {
            val addLayout = layoutInflater.inflate(R.layout.word_add_layout,null)
            val addDialog = AlertDialog.Builder(requireActivity())
            addDialog.setView(addLayout)
                .setTitle(R.string.add_word_dialog_title)
                .setPositiveButton(R.string.yes){ _, _ ->
                    val word = Word(0,addLayout.editTextWordAddEnglish.text.toString(),addLayout.editTextWordAddChinese.text.toString())
                    viewModel.insert(word)
                }
                .setNegativeButton(R.string.no){_, _ ->
                }
            addDialog.create()
            addDialog.show()
        }
    }

}
