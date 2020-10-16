package com.martianlab.recipes.presentation.fragments.details

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.martianlab.recipes.App
import com.martianlab.recipes.domain.RecipesInteractor
import com.martianlab.recipes.entities.*
import com.martianlab.recipes.presentation.RecipeComplexity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.properties.Delegates

class DetailsViewModel(

) : ViewModel(), CoroutineScope by CoroutineScope(Dispatchers.IO) {

    @Inject
    lateinit var interactor: RecipesInteractor

    var recipeId : Long? by Delegates.observable(null){ _, _, new ->
        launch { getReceipt(new) }
    }

    val stagesList = ObservableField<List<RecipeStage>>()
    val ingredientsList = ObservableField<List<RecipeIngredient>>()
    val personsText = ObservableField<String>()
    val complexity = ObservableField<String>()
    val title = ObservableField<String>()
    val imageURL = ObservableField<String>()

    init {
        App.component.inject(this)
    }

    internal suspend fun getReceipt(recipeId : Long?){
        recipeId?.let {
            interactor.getRecipe(recipeId)?.let {
                loadDetailsToView(it)
            }
        }
    }

    internal fun loadDetailsToView(recipe : Recipe ){
        personsText.set( "Блюдо на " + recipe.personCount + " персоны")
        complexity.set("Сложность приготовления: " + RecipeComplexity.getByNum(recipe.complexity))
        title.set(recipe.title)
        imageURL.set(recipe.imageURL)
        stagesList.set(recipe.stages)
        ingredientsList.set(recipe.ingredients)
    }

}