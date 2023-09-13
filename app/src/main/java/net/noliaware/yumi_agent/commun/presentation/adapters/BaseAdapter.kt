package net.noliaware.yumi_agent.commun.presentation.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseAdapter<T>(private val dataSet: List<T>) : RecyclerView.Adapter<BaseViewHolder<T>>() {

    var expressionViewHolderBinding: ((T, View) -> Unit)? = null
    var expressionOnCreateViewHolder: ((ViewGroup) -> View)? = null
    var onItemClicked: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<T> {
        return expressionOnCreateViewHolder?.let {
            it(parent)
        }?.let { view ->
            BaseViewHolder(
                view = view,
                expression = expressionViewHolderBinding,
                onItemClicked = onItemClicked?.let {
                    { position ->
                        dataSet[position]?.let {
                            onItemClicked?.invoke(position)
                        }
                    }
                }
            )
        }!!
    }

    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}