package com.riggle.data.models.response

data class DeliverySlots(var weekdata: ArrayList<DeliveryDateSlots>)

data class DeliveryDateSlots(var title: String,
                             var sub_title: String,
                             var value: String,
                             var timeSlots: ArrayList<String>,

                             )