package depauw.datle.instagramy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import depauw.datle.instagramy.Post
import depauw.datle.instagramy.R
import depauw.datle.instagramy.databinding.FragmentHomeBinding

open class HomeFragment : Fragment() {
    private val sc_refreshListener = SwipeRefreshLayout.OnRefreshListener() {
        adapter.clear()
        queryPosts(MAX_POST)
    }
    protected lateinit var binding: FragmentHomeBinding
    protected lateinit var adapter: HomeFragmentAdapter
    protected var listPosts = ArrayList<Post>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.swipeContainer.setOnRefreshListener(sc_refreshListener)
        binding.swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light)

        adapter = HomeFragmentAdapter(listPosts, requireContext())
        binding.rvPost.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPost.adapter = adapter

        queryPosts(MAX_POST)
    }

    open fun queryPosts(limit: Int) {
        val parseQuery: ParseQuery<Post> = ParseQuery.getQuery(
            Post::class.java
        )
        parseQuery.include(Post.KEY_USER)
        parseQuery.addDescendingOrder("createdAt")
        parseQuery.limit = limit
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
    companion object {
        val TAG = "HomeFragment"
        val MAX_POST = 20
    }
}

