import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.kashifshahazad.smartagriassistant.CropIssue
import com.kashifshahazad.smartagriassistant.CropIssueViewHolder
import com.kashifshahazad.smartagriassistant.Details_of_cropIssue
import com.kashifshahazad.smartagriassistant.add_crop_issue
import com.kashifshahazad.smartagriassistant.databinding.ItemCropIssueBinding

class CropIssueAdapter(val items: ArrayList<CropIssue>) : RecyclerView.Adapter<CropIssueViewHolder>() {

    // Use sizes as a mutable list to hold the data for the adapter
    private var sizes: List<CropIssue> = items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropIssueViewHolder {
        Log.i("SizeAdapter", "onCreateViewHolder")
        return CropIssueViewHolder(
            ItemCropIssueBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return sizes.size // Return the size of the sizes list
    }

    override fun onBindViewHolder(holder: CropIssueViewHolder, position: Int) {

        val item = sizes[position]
        holder.binding.textView31.text = item.title?.toString()
        holder.binding.textView32.text = item.description?.toString()
        holder.binding.textView28.text = item.date?.toString()
        holder.binding.textView7.text = item.noComments?.toString()



        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, Details_of_cropIssue::class.java)
            intent.putExtra("data", Gson().toJson(item))
            holder.itemView.context.startActivity(intent)
        }
    }

    // Update the list of sizes with new data and refresh the RecyclerView
//    fun updateList(newSizes: List<Size>) {
//        sizes = newSizes
//        notifyDataSetChanged()
//    }
}
