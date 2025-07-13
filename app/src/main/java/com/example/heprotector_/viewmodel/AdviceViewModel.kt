package com.example.heprotector_.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heprotector_.data.db.entities.Local_Food
import com.example.heprotector_.data.repository.ExerciseSuggestionRepository
import com.example.heprotector_.data.repository.HealthRecordRepository
import com.example.heprotector_.data.repository.LocalFoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdviceViewModel(
    private val healthRecordRepository: HealthRecordRepository,
    private val exerciseSuggestionRepository: ExerciseSuggestionRepository,
    private val localFoodRepository: LocalFoodRepository,
    private val sharedViewModel: SharedViewModel
) : ViewModel() {

    private val _exerciseAdvice = MutableStateFlow("Loading exercise advice...")
    val exerciseAdvice: StateFlow<String> = _exerciseAdvice.asStateFlow()

    private val _foodAdvice = MutableStateFlow("Loading food advice...")
    val foodAdvice: StateFlow<String> = _foodAdvice.asStateFlow()

    private val _localFoods = MutableStateFlow<List<Local_Food>>(emptyList())
    val localFoods: StateFlow<List<Local_Food>> = _localFoods.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        // Observe currentUserId to trigger advice generation
        viewModelScope.launch {
            sharedViewModel.currentUserId.collect { userId ->
                if (userId != null) {
                    generateAdvice(userId)
                    loadLocalFoods() // Load foods once user is logged in
                } else {
                    _exerciseAdvice.value = "Login to get personalized exercise advice."
                    _foodAdvice.value = "Login to get personalized food advice."
                    _localFoods.value = emptyList()
                }
            }
        }
    }

    fun dismissMessage() { _message.value = null }


    private suspend fun generateAdvice(userId: Int) {
        // Fetch latest health record for personalization
        val latestRecord = healthRecordRepository.getLatestHealthRecordForUser(userId)

        // --- Exercise Advice Logic ---
        val generatedExerciseAdvice = when {
            latestRecord?.weight != null && latestRecord.weight > 90.0 -> "Consider low-impact exercises like swimming or cycling to protect your joints."
            latestRecord?.bloodSugar != null && latestRecord.bloodSugar > 180.0 -> "Regular, moderate intensity exercise like brisk walking can help manage blood sugar. Consult your doctor first."
            else -> "Aim for at least 30 minutes of moderate-intensity exercise most days of the week. Examples include brisk walking, jogging, or dancing."
        }
        _exerciseAdvice.value = generatedExerciseAdvice

        // Also fetch and display recent suggestions if any were previously generated
        exerciseSuggestionRepository.getRecentExerciseSuggestionsForUser(userId)
            .collect { suggestions ->
                if (suggestions.isNotEmpty()) {
                    val recentSuggestionText = suggestions.joinToString("\n") { "- ${it.name}: ${it.description}" }
                    _exerciseAdvice.value += "\n\nRecent tailored suggestions:\n$recentSuggestionText"
                }
            }


        // --- Food Advice Logic ---
        val generatedFoodAdvice = when {
            latestRecord?.bloodSugar != null && latestRecord.bloodSugar > 180.0 -> "Focus on foods with a low glycemic index, such as whole grains, vegetables, and lean proteins. Limit sugary drinks and refined carbs."
            latestRecord?.weight != null && latestRecord.weight > 80.0 -> "Prioritize portion control, lean proteins, and plenty of fiber-rich vegetables. Reduce intake of fried foods and excessive fats."
            else -> "Maintain a balanced diet with a variety of fruits, vegetables, lean proteins, and whole grains. Stay hydrated."
        }
        _foodAdvice.value = generatedFoodAdvice
    }

    private fun loadLocalFoods() {
        viewModelScope.launch {
            localFoodRepository.getAllLocalFoods().collect { foods ->
                _localFoods.value = foods
            }
        }
    }

    // You can add more specific functions here, e.g., to search local foods
    fun searchLocalFoods(query: String) {
        viewModelScope.launch {
            if (query.isNotBlank()) {
                localFoodRepository.searchLocalFoods(query).collect { foods ->
                    _localFoods.value = foods
                }
            } else {
                loadLocalFoods() // If query is empty, show all foods
            }
        }
    }

    // Example of inserting some initial local food data (for demo purposes)
    fun populateInitialLocalFoods() {
        viewModelScope.launch {
            val foods = listOf(
                Local_Food(foodName = "Phở Bò", nutritionalInfo = "High protein, moderate carbs. Contains rice noodles, beef broth, beef."),
                Local_Food(foodName = "Bún Chả", nutritionalInfo = "Grilled pork with vermicelli noodles and dipping sauce. Balanced meal if consumed moderately."),
                Local_Food(foodName = "Gỏi Cuốn (Fresh Spring Rolls)", nutritionalInfo = "Low calorie, fresh vegetables, shrimp/pork. Healthy option."),
                Local_Food(foodName = "Cơm Tấm (Broken Rice)", nutritionalInfo = "Broken rice with grilled pork chop, egg, and pork skin. Higher in calories and fat."),
                Local_Food(foodName = "Bánh Mì", nutritionalInfo = "Vietnamese baguette sandwich. Nutritional info varies greatly by filling. Can be high in sodium and fat.")
            )
            foods.forEach { localFoodRepository.insertLocalFood(it) }
            _message.value = "Initial local food data populated."
        }
    }
}