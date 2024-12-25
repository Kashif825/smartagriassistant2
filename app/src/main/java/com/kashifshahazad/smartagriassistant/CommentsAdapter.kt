import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kashifshahazad.smartagriassistant.Comment
import com.kashifshahazad.smartagriassistant.CommentViewHolder
import com.kashifshahazad.smartagriassistant.CropIssue
import com.kashifshahazad.smartagriassistant.CropIssueViewHolder
import com.kashifshahazad.smartagriassistant.Details_of_cropIssue
import com.kashifshahazad.smartagriassistant.add_crop_issue
import com.kashifshahazad.smartagriassistant.databinding.ItemCommentsBinding
import com.kashifshahazad.smartagriassistant.databinding.ItemCropIssueBinding

class CommentsAdapter(val items: ArrayList<Comment>) : RecyclerView.Adapter<CommentViewHolder>() {

    // Use sizes as a mutable list to hold the data for the adapter
    private var sizes: List<Comment> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        Log.i("SizeAdapter", "onCreateViewHolder")
        return CommentViewHolder(
            ItemCommentsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return sizes.size // Return the size of the sizes list
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

        val item = sizes[position]
        holder.binding.textView31.text = item.comment?.toString()
        if(item.isAccpetd==true){
            holder.binding.cardView2.visibility= View.VISIBLE

        }else {
            holder.binding.cardView2.visibility= View.GONE
        }



        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, Details_of_cropIssue::class.java)
            intent.putExtra("data", Gson().toJson(item))
            holder.itemView.context.startActivity(intent)
        }
    }

}
