if (!isLoggedIn()) {
    window.location.href = 'index.html';
}

let currentUser = null;

function getToday() {
    return new Date().toISOString().split('T')[0];
}

function getSelectedDate() {
    return document.getElementById('summary-date').value || getToday();
}

function refresh() {
    loadSummary();
    loadMeals();
}

async function init() {
    currentUser = await getCurrentUser();
    if (!currentUser) return;

    document.getElementById('summary-date').value = getToday();
    refresh();
}

async function loadSummary() {
    const date = getSelectedDate();
    if (!currentUser) return;

    const summary = await apiRequest('/stats/nutrition-summary/' + currentUser.id + '?date=' + date, 'GET');

    document.getElementById('sum-calories').textContent = Math.round(summary.totalCalories);
    document.getElementById('sum-protein').textContent = Math.round(summary.totalProtein) + 'г';
    document.getElementById('sum-carbs').textContent = Math.round(summary.totalCarbs) + 'г';
    document.getElementById('sum-fat').textContent = Math.round(summary.totalFat) + 'г';
}

document.getElementById('summary-date').addEventListener('change', refresh);

let searchTimeout;
document.getElementById('food-search').addEventListener('input', function() {
    clearTimeout(searchTimeout);
    const query = this.value;

    if (query.length < 2) {
        document.getElementById('food-results').innerHTML = '';
        return;
    }

    searchTimeout = setTimeout(async function() {
        const foods = await apiRequest('/foods/search?name=' + encodeURIComponent(query), 'GET');
        renderFoodResults(foods);
    }, 300);
});

function renderFoodResults(foods) {
    const resultsEl = document.getElementById('food-results');
    resultsEl.innerHTML = '';

    foods.forEach(function(food) {
        const item = document.createElement('div');
        item.className = 'food-result-item';
        item.innerHTML =
            '<span>' + food.name + '</span>' +
            '<span class="kcal numeric">' + food.caloriesPer100g + ' ккал/100г</span>';

        item.addEventListener('click', function() {
            document.getElementById('selected-food-id').value = food.id;
            renderSelectedChip(food.name);
            document.getElementById('food-search').value = '';
            resultsEl.innerHTML = '';
        });

        resultsEl.appendChild(item);
    });
}

function renderSelectedChip(name) {
    const chipEl = document.getElementById('selected-food-chip');
    chipEl.innerHTML =
        '<div class="food-chip">' +
            '<span>' + name + '</span>' +
            '<i class="ti ti-x" id="clear-chip-btn"></i>' +
        '</div>';

    document.getElementById('clear-chip-btn').addEventListener('click', function() {
        document.getElementById('selected-food-id').value = '';
        chipEl.innerHTML = '';
    });
}

async function loadMeals() {
    const date = getSelectedDate();
    const meals = await apiRequest('/meal-logs/user/' + currentUser.id + '?date=' + date, 'GET');
    renderMeals(meals);
}

function renderMeals(meals) {
    const listEl = document.getElementById('meals-list');
    listEl.innerHTML = '';

    if (meals.length === 0) {
        listEl.innerHTML =
            '<div class="card empty-state">' +
                '<i class="ti ti-salad" aria-hidden="true"></i>' +
                '<p>Няма добавени хранения за тази дата</p>' +
            '</div>';
        return;
    }

    meals.forEach(function(meal) {
        const card = document.createElement('div');
        card.className = 'card card-compact row-between';
        card.innerHTML =
            '<div>' +
                '<p class="item-title">' + meal.food.name + '</p>' +
                '<p class="item-sub numeric">' + meal.grams + ' г</p>' +
            '</div>' +
            '<button class="delete-meal-btn btn-danger" data-id="' + meal.id + '">Изтрий</button>';
        listEl.appendChild(card);
    });

    document.querySelectorAll('.delete-meal-btn').forEach(function(btn) {
        btn.addEventListener('click', async function() {
            const id = btn.getAttribute('data-id');
            await apiRequest('/meal-logs/' + id, 'DELETE');
            refresh();
        });
    });
}

document.getElementById('add-meal-btn').addEventListener('click', async function() {
    const foodId = document.getElementById('selected-food-id').value;
    const grams = document.getElementById('meal-grams').value;
    const errorEl = document.getElementById('meal-error');

    errorEl.textContent = '';

    if (!foodId || !grams) {
        errorEl.textContent = 'Моля, изберете храна и въведете грамаж.';
        return;
    }

    try {
        await apiRequest('/meal-logs', 'POST', {
            date: getSelectedDate(),
            grams: parseFloat(grams),
            user: { id: currentUser.id },
            food: { id: parseInt(foodId) }
        });

        document.getElementById('meal-grams').value = '';
        document.getElementById('selected-food-id').value = '';
        document.getElementById('selected-food-chip').innerHTML = '';
        refresh();
    } catch (err) {
        errorEl.textContent = err.message || 'Възникна грешка.';
    }
});

init();