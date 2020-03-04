package com.shanya.leaningassistant.word

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.shanya.leaningassistant.R
import kotlinx.android.synthetic.main.word_item_layout.view.*

class WordAdapter(context: Context,private val wordViewModel: WordViewModel):
    ListAdapter<Word,WordAdapter.WordViewHolder>(object : DiffUtil.ItemCallback<Word>(){
        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.word == newItem.word &&
                    oldItem.chineseMeaning == newItem.chineseMeaning &&
                    oldItem.chineseInvisible == newItem.chineseInvisible
        }

        @Suppress("DEPRECATED_IDENTITY_EQUALS")
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id === newItem.id
        }
    }){

    private val inflater: LayoutInflater = LayoutInflater.from(context)


    inner class WordViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textViewWordNum: TextView = itemView.findViewById(R.id.textViewWordNum)
        val textViewWordEnglish: TextView = itemView.findViewById(R.id.textViewWordEnglish)
        val textViewWordChinese: TextView = itemView.findViewById(R.id.textViewWordChinese)
        val aSwitchChineseInvisible: Switch = itemView.findViewById(R.id.switchChineseInvisible)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.word_item_layout,parent,false)
        val holder = WordViewHolder(itemView)
        holder.itemView.setOnClickListener {
            val uri = Uri.parse("http://m.youdao.com/dict?le=eng&q=" + holder.textViewWordEnglish.text)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(uri)
            holder.itemView.context.startActivity(intent)
        }
        holder.aSwitchChineseInvisible.setOnCheckedChangeListener { buttonView, isChecked ->
            val word: Word = holder.itemView.getTag(R.id.word_for_view_holder) as Word
            if (isChecked){
                holder.textViewWordChinese.visibility = View.GONE
                word.chineseInvisible = true
                wordViewModel.update(word)
            }else{
                holder.textViewWordChinese.visibility = View.VISIBLE
                word.chineseInvisible = false
                wordViewModel.update(word)
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val word:Word = getItem(position)
        holder.itemView.setTag(R.id.word_for_view_holder,word)
        holder.textViewWordNum.text = (position + 1).toString()
        holder.textViewWordEnglish.text = word.word
        holder.textViewWordChinese.text = word.chineseMeaning
        if (word.chineseInvisible){
            holder.textViewWordChinese.visibility = View.GONE
            holder.aSwitchChineseInvisible.isChecked = true
        }else{
            holder.textViewWordChinese.visibility = View.VISIBLE
            holder.aSwitchChineseInvisible.isChecked = false
        }
    }

    override fun onViewAttachedToWindow(holder: WordViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.textViewWordNum.text = (holder.adapterPosition + 1).toString()
    }
}