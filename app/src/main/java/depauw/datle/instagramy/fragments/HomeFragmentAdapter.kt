package depauw.datle.instagramy.fragments

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import depauw.datle.instagramy.Post
import depauw.datle.instagramy.R

class HomeFragmentAdapter(private val listPosts: ArrayList<Post>, private val context: Context):
    RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder>() {


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvUsername = view.findViewById<TextView>(R.id.tv_username)
        var tvDescription = view.findViewById<TextView>(R.id.tv_description)
        var ivPhoto = view.findViewById<ImageView>(R.id.iv_photo)

        fun bind(post: Post) {
            tvUsername.text = post.getUser()?.username
            tvDescription.text = post.getDescription()
            Glide.with(itemView.context).load(post.getImage()?.url).into(ivPhoto)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listPosts[position])
    }

    override fun getItemCount(): Int {
        return listPosts.size
    }
    fun clear() {
        listPosts.clear()
        notifyDataSetChanged()
    }
    // Add a list of items -- change to type used

    fun addAll(listPosts: ArrayList<Post>) {
        this.listPosts.addAll(listPosts)
        notifyDataSetChanged()

    }
    companion object {
        val TAG = "HomeFragmentAdapter"
    }
}
