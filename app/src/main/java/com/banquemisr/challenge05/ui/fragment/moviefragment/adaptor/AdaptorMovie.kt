package com.banquemisr.challenge05.ui.fragment.moviefragment.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.banquemisr.challenge05.BuildConfig
import com.banquemisr.challenge05.R
import com.banquemisr.challenge05.data.model.Genre
import com.banquemisr.challenge05.data.model.Result
import com.banquemisr.challenge05.data.model.SourceModel.Companion.baseUrlImage
import com.banquemisr.challenge05.databinding.OneItemCategoryBinding
import com.banquemisr.challenge05.databinding.OneItemMovieBinding


class AdaptorMovie( // one selection
    var context: Context, var arrayList: ArrayList<Result>,
    val callBackClick: (Result) -> Unit
)
   // var selectedArrayList: ArrayList<ModelTrip>
 :
    RecyclerView.Adapter<AdaptorMovie.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = OneItemMovieBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(
            binding
        )

    }

    override fun getItemCount(): Int {

        return arrayList.size

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(arrayList[position])

    }
   inner class ViewHolder(val itemViews: OneItemMovieBinding) : RecyclerView.ViewHolder(itemViews.root) {
       fun bindItems(
           currentItem: Result?
       ) {
           itemViews.name.text = currentItem?.title?:""
           itemViews.yearOfProduction.text = currentItem?.release_date?:""
           Glide.with(itemViews.root.context).load(baseUrlImage+currentItem?.poster_path)
               .error(R.color.gray_new).placeholder(R.color.gray_new).dontAnimate().into(itemViews.movieImage)
           itemViews.cardOneItem.setOnClickListener{
               callBackClick.invoke(currentItem!!)
           }
       }


   }
    fun updateList(results: List<Result>) {
     //   if (arrayList.isEmpty()) {
            arrayList.addAll(results)
            notifyDataSetChanged()
      /*  }
        else {
            arrayList.addAll(results)
            notifyItemRangeChanged(0,arrayList.size)
        }*/
    }

}