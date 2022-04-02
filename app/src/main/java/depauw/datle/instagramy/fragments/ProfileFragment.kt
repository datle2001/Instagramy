package depauw.datle.instagramy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import depauw.datle.instagramy.Post
import depauw.datle.instagramy.R

class ProfileFragment : HomeFragment() {

    override fun queryPosts(limit: Int) {
        val parseQuery: ParseQuery<Post> = ParseQuery.getQuery(
            Post::class.java
        )
        parseQuery.include(Post.KEY_USER)
        parseQuery.addDescendingOrder("createdAt")
        parseQuery.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        // This query can even be more granular (i.e. only refresh if the entry was added by some other user)
        // parseQuery.whereNotEqualTo(USER_ID_KEY, ParseUser.getCurrentUser().getObjectId());
        // Connect to Parse server
        parseQuery.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e != null) {
                    Log.e(TAG, "Error fetching posts")
                }
                else {
                    if(posts != null) {
                        listPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                        binding.swipeContainer.isRefreshing = false
                    }
                }
            }
        })
    }
}