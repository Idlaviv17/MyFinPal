package angulo.javier.myfinpal.util

import android.widget.ImageView
import angulo.javier.myfinpal.R

object IconHandler {
    fun setIcon(category: String, iconBgImageView: ImageView, iconImageView: ImageView) {
        when (category) {
            Categories.FOOD.stringValue -> {
                iconBgImageView.setBackgroundResource(R.drawable.budget_menu_food_background)
                iconImageView.setImageResource(R.drawable.budget_menu_food)
            }
            Categories.SHOPPING.stringValue -> {
                iconBgImageView.setBackgroundResource(R.drawable.budget_menu_shopping_background)
                iconImageView.setImageResource(R.drawable.budget_menu_shopping)
            }
            Categories.HEALTH.stringValue -> {
                iconBgImageView.setBackgroundResource(R.drawable.budget_menu_health_background)
                iconImageView.setImageResource(R.drawable.budget_menu_health)
            }
            Categories.ACTIVITIES.stringValue -> {
                iconBgImageView.setBackgroundResource(R.drawable.budget_menu_activities_background)
                iconImageView.setImageResource(R.drawable.budget_menu_activities)
            }
            Categories.MEMBERSHIPS.stringValue -> {
                iconBgImageView.setBackgroundResource(R.drawable.budget_menu_membership_background)
                iconImageView.setImageResource(R.drawable.budget_menu_membership)
            }
            Categories.RESTAURANTS.stringValue -> {
                iconBgImageView.setBackgroundResource(R.drawable.budget_menu_restaurants_background)
                iconImageView.setImageResource(R.drawable.budget_menu_restaurants)
            }
        }
    }
}