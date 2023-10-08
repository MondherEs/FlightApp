package com.creative.flightapp.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.creative.flightapp.R
import com.creative.flightapp.model.FlightResponse
import com.creative.flightapp.view.FlightDetailActivity


class FlightAdapter(var context: Context, var flightDetail: List<FlightResponse.FlightResponseItem>?) : RecyclerView.Adapter<FlightAdapter.ViewHolder>() {



    @JvmName("setInvoices1")
    fun getFlight(flightList: List<FlightResponse.FlightResponseItem>?) {
        this.flightDetail = flightList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.flight_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight = flightDetail!![holder.adapterPosition]
        holder.departureDate.text = flight.departuredate
        holder.arrivalDate.text = flight.arrivaldate
        holder.arrivalCountry.text = flight.destination
        holder.departCountry.text = flight.arival
        holder.price.text = flight.price

        holder.moreDetails.setOnClickListener {
            val intent = Intent(context.applicationContext, FlightDetailActivity::class.java)
            intent.putExtra("flightPos", position+1)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (flightDetail != null) { flightDetail!!.size } else 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         var departureDate: TextView
         var arrivalDate: TextView
         var departCountry: TextView
         var arrivalCountry: TextView
         var price: TextView
         var moreDetails: Button

        init {
            departureDate = itemView.findViewById(R.id.startDate)
            arrivalDate = itemView.findViewById(R.id.endDate)
            departCountry = itemView.findViewById(R.id.arrival)
            arrivalCountry = itemView.findViewById(R.id.destination)
            price = itemView.findViewById(R.id.price)
            moreDetails = itemView.findViewById(R.id.moredetail)

        }
    }
}