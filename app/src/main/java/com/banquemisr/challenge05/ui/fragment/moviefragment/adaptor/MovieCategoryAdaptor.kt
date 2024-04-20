package com.banquemisr.challenge05.ui.fragment.moviefragment.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.banquemisr.challenge05.R
import com.banquemisr.challenge05.data.model.Genre
import com.banquemisr.challenge05.databinding.OneItemCategoryBinding
import com.google.android.material.card.MaterialCardView


class MovieCategoryAdaptor( // one selection
    var context: Context, var arrayList: ArrayList<Genre>,
    val callBackClickStory: (Genre) -> Unit,var selectedItemId : Int
)
   // var selectedArrayList: ArrayList<ModelTrip>
 :
    RecyclerView.Adapter<MovieCategoryAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = OneItemCategoryBinding.inflate(LayoutInflater.from(context),parent,false)

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
   inner class ViewHolder(val itemViews: OneItemCategoryBinding) : RecyclerView.ViewHolder(itemViews.root) {
       fun bindItems(
           currentItem: Genre?
       ) {
           if (currentItem?.id==selectedItemId)
               setBackgroundTint(itemViews.cardOneItem, R.color.black,android.R.color.transparent
                   ,R.color.white,itemViews.genreName)
           else
               setBackgroundTint(itemViews.cardOneItem, R.color.white,R.color.gray_new,R.color.gray_new,itemViews.genreName)
           itemViews.genreName.text = currentItem?.name
           itemViews.cardOneItem.setOnClickListener{
               selectedItemId = currentItem?.id?:0
               notifyDataSetChanged()
               callBackClickStory.invoke(currentItem!!)
           }
       }
   }
    private fun setBackgroundTint(
        selectedCard: MaterialCardView,
        selectedColor: Int,
        colorStroke: Int,
        textColor: Int,
        genreName: TextView,
    ) {
            selectedCard.backgroundTintList = ContextCompat.getColorStateList(selectedCard.context,selectedColor)
            selectedCard.setStrokeColor(ContextCompat.getColorStateList(selectedCard.context,colorStroke))
        genreName.setTextColor(ContextCompat.getColor(genreName.context,textColor))
    }



}