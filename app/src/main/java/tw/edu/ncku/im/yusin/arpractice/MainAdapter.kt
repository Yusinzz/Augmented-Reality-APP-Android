package tw.edu.ncku.im.yusin.arpractice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import tw.edu.ncku.im.yusin.arpractice.databinding.ListBinding



class MainAdapter(private var menulist :MutableList<menu>) :RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private lateinit var menulistener: onMenuClicklistener
    interface onMenuClicklistener{
        fun onClick(position: Int)
    }
    fun setOnMenuClicklistener(listener:onMenuClicklistener){
        menulistener = listener
    }

    class ViewHolder(itemView: View, listener: onMenuClicklistener):  RecyclerView.ViewHolder(itemView) {

        val imageView :ImageView = itemView.findViewById(R.id.image)
        val textView: TextView = itemView.findViewById(R.id.text)

        init {
            itemView.setOnClickListener{
                listener.onClick(adapterPosition)
            }

        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list, parent, false)

        return ViewHolder(view,menulistener)
    }

    override fun getItemCount(): Int { //顯示recycleview的數量
        return menulist.count()
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = menulist[position]
        holder.imageView.setImageResource(current.img)
        holder.textView.setText(current.text)


    }
}
