package com.riggle.ui.bottomsheets

import android.os.Bundle
import android.provider.MediaStore.Video
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.riggle.R
import com.riggle.data.DataManager
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.RequestCartData
import com.riggle.data.models.request.VariantUpdate
import com.riggle.data.models.response.ProductsData
import com.riggle.data.models.response.SchemesBean
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.listener.ProductVariantListener
import com.riggle.ui.other.adapter.ProductsVariantAdapter
import kotlinx.android.synthetic.main.bottom_sheet_product_variant.view.*
import org.koin.android.ext.android.inject
import java.util.*
import kotlin.collections.ArrayList


class ProductVariantSheet : BottomSheetDialogFragment(),
    ProductsVariantAdapter.HomeProductsListener {
    /*@JvmField
    @Inject
    var dataManager: DataManager? = null*/

    val dataManager: DataManager by inject()


    private var product_id = 0
    var schemeList: ArrayList<SchemesBean>? = null
    private val products: ArrayList<VariantUpdate> = ArrayList()
    private var listener: ProductVariantListener? = null
    fun setListener(listener: ProductVariantListener?) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //RiggleApplication.getInstance().getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_product_variant, container, false)
    }

    private val mBottomSheetBehaviorCallback: BottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            view.viewTreeObserver.addOnGlobalLayoutListener {
                val dialog = dialog as BottomSheetDialog?
                val bottomSheet =
                    dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)

                bottomSheet?.let {
                    val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior.addBottomSheetCallback(mBottomSheetBehaviorCallback)
                    behavior.peekHeight = 0
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val bundle = arguments
        if (bundle != null) {
            product_id = bundle.getInt("product_id", 0)
            schemeList = Gson().fromJson(
                bundle.getString("scheme"),
                object : TypeToken<ArrayList<SchemesBean>>() {}.type
            )
        }
        //getProductVariants()
        populateRecyclerView(ArrayList<ProductsData>())
        view.ivCross?.setOnClickListener { dismiss() }
    }

    private fun getProductVariants() {
        dataManager.getVariants(object :
            ApiResponseListener<APICommonResponse<ArrayList<ProductsData>>> {
            override fun onSuccess(response: APICommonResponse<ArrayList<ProductsData>>) {
                if (response.isSuccess && response.data != null && response.data!!.size > 0) {
                    populateRecyclerView(response.data)
                } else {
                    /*Toast.makeText(
                        activity,
                        activity?.getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
            }

            override fun onError(apiError: ApiError???) {
                /*Toast.makeText(
                    activity,
                    activity?.getString(R.string.error),
                    Toast.LENGTH_SHORT
                ).show()*/
            }
        }, product_id)
    }


/*
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
       *//* ivCross = requireView().findViewById(R.id.ivCross)
        ivImg = view?.findViewById(R.id.ivImg)
        tvName = view?.findViewById(R.id.tvName)
        rvVariant = view?.findViewById(R.id.rvVariants)
        tvDone = view?.findViewById(R.id.tvDone)*//*
        val bundle = arguments
        if (bundle != null) {
            product_id = bundle.getInt("product_id", 0)
        }
        productVariants
        ivCross?.setOnClickListener(View.OnClickListener { dismiss() })
    }*/

    private fun populateRecyclerView(data: ArrayList<ProductsData>?) {
        data?.let { data ->
            /*if (data.size > 0) {*/
            view?.rootView?.let { rootView ->
                activity?.let { activity ->
                    /*Glide.with(requireActivity())
                        .load(data[0].image)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(rootView.ivImg)

                    rootView.tvName?.text = data[0].name*/
                    val adapter = ProductsVariantAdapter(requireActivity(), data)
                    rootView.rvVariants?.layoutManager = LinearLayoutManager(activity)
                    rootView.rvVariants?.adapter = adapter
                    adapter.setListener(this)
                    rootView.tvDone?.setOnClickListener { updateCart() }
                }
            }
            /*}*/
        }
    }

    private fun updateCart() {
        showHideLoader(true)
        if (products != null && products.size > 0) {

            val cartData = RequestCartData(products)


            dataManager?.addCart(object : ApiResponseListener<APICommonResponse<ProductsData>> {
                override fun onSuccess(response: APICommonResponse<ProductsData>) {
                    showHideLoader(false)
                    response?.let { response ->
                        if (response.isSuccess && response.data != null) {
                            products.clear()
                            if (listener != null) listener?.itemUpdated(response.data!!)
                            //dismiss();
                        }
                    }

                }

                override fun onError(apiError: ApiError??) {
                    showHideLoader(false)
                    Toast.makeText(
                        activity,
                        activity?.getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }, cartData)

        } else dismiss()
    }

    /*
        @Override
        public void variantAdded(int product_id, int quantity) {
            if (products.size() > 0) {
                for (int i = 0; i < products.size(); i++) {
                    boolean productFound = false;
                    if (products.get(i).getProduct_id() == product_id) {
                        VariantUpdate update = new VariantUpdate(product_id, quantity);
                        products.set(i, update);
                        productFound = true;
                    }
                    if ((i == products.size() - 1) && !productFound) {
                        VariantUpdate update = new VariantUpdate(product_id, quantity);
                        products.add(update);
                    }
                }
            } else {
                VariantUpdate update = new VariantUpdate(product_id, quantity);
                products.add(update);
            }

            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getQuantity() == 0)
                    products.remove(i);
            }
        }*/
    override fun variantAdded(product_id: Int, quantity: Int) {
        val update = VariantUpdate(product_id, quantity,null)
        products?.add(update)
        updateCart()
    }

    private var loadingDialog: LoadingDialog? = null
    private fun showHideLoader(state: Boolean) {
        if (loadingDialog != null) {
            if (state) loadingDialog?.show() else loadingDialog?.hide()
        } else {
            loadingDialog = LoadingDialog(activity)
            showHideLoader(state)
        }
    }
}