package com.bilalmirza.newsnow.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bilalmirza.newsnow.databinding.ItemArticlePreviewBinding
import com.bilalmirza.newsnow.model.Article
import com.bumptech.glide.Glide

//  class name here below should be the same as the name here below
//    <--------->                       <--------->
class NewsAdapter: RecyclerView.Adapter<NewsAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemArticlePreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(currentItem.image).into(holder.binding.articleImage)
            holder.binding.articleSource.text = currentItem.source.name
            holder.binding.articleTitle.text = currentItem.title
            holder.binding.articleDescription.text = currentItem.description
            holder.binding.articleDateTime.text = currentItem.publishedAt

            setOnClickListener {
                onItemClickListener?.let {
                    it(currentItem)
                }
            }
        }
    }
    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setonItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}