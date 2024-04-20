package com.banquemisr.challenge05.ui.fragment.moviedetails.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.banquemisr.challenge05.data.model.ProductionCompany
import com.banquemisr.challenge05.databinding.OneItemProductionBinding


class ProductionCompanyAdaptor( // one selection
    var context: Context, var arrayList: ArrayList<ProductionCompany>,
)
   // var selectedArrayList: ArrayList<ModelTrip>
 :
    RecyclerView.Adapter<ProductionCompanyAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = OneItemProductionBinding.inflate(LayoutInflater.from(context),parent,false)

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
   inner class ViewHolder(val itemViews: OneItemProductionBinding) : RecyclerView.ViewHolder(itemViews.root) {
       fun bindItems(
           currentItem: ProductionCompany?
       ) {
           itemViews.productionCompany.text = currentItem?.name?:""

       }


   }

}