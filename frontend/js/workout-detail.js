if (!isLoggedIn()) {
    window.location.href = 'index.html';
}

const params = new URLSearchParams(window.location.search);
const workoutId = params.get('id');

if (!workoutId) {
    window.location.href = 'workouts.html';
}

async function init() {
    const workout = await apiRequest('/workouts/' + workoutId, 'GET');
    document.getElementById('workout-title').textContent = workout.notes || 'Тренировка';
    document.getElementById('workout-date').textContent = workout.date;

    await loadExercises();
    await loadSets();
    await loadVolume();
}

async function loadExercises() {
    const exercises = await apiRequest('/exercises', 'GET');
    const selectEl = document.getElementById('exercise-select');
    selectEl.innerHTML = '';

    exercises.forEach(function(exercise) {
        const option = document.createElement('option');
        option.value = exercise.id;
        option.textContent = exercise.name + ' (' + exercise.muscleGroup + ')';
        selectEl.appendChild(option);
    });
}

async function loadVolume() {
    const volume = await apiRequest('/stats/workout-volume/' + workoutId, 'GET');
    document.getElementById('total-volume').textContent = volume;
}

async function loadSets() {
    const sets = await apiRequest('/workout-sets/workout/' + workoutId, 'GET');
    renderSets(sets);
}

function groupByExercise(sets) {
    const groups = {};
    sets.forEach(function(set) {
        const key = set.exercise.id;
        if (!groups[key]) {
            groups[key] = { name: set.exercise.name, items: [] };
        }
        groups[key].items.push(set);
    });
    return groups;
}

function renderSets(sets) {
    const listEl = document.getElementById('sets-list');
    listEl.innerHTML = '';

    if (sets.length === 0) {
        listEl.innerHTML =
            '<div class="card empty-state">' +
                '<i class="ti ti-list-details" aria-hidden="true"></i>' +
                '<p>Все още нямате добавени серии</p>' +
            '</div>';
        return;
    }

    const groups = groupByExercise(sets);

    Object.keys(groups).forEach(function(exerciseId) {
        const group = groups[exerciseId];
        const card = document.createElement('div');
        card.className = 'card';

        let rowsHtml = '';
        group.items.forEach(function(set, index) {
            rowsHtml +=
                '<div class="set-row row-between">' +
                    '<p class="numeric item-title">Серия ' + (index + 1) + ': ' + set.reps + ' x ' + set.weightKg + ' кг</p>' +
                    '<button class="delete-set-btn btn-danger" data-id="' + set.id + '">Изтрий</button>' +
                '</div>';
        });

        card.innerHTML = '<p class="group-title">' + group.name + '</p>' + rowsHtml;
        listEl.appendChild(card);
    });

    document.querySelectorAll('.delete-set-btn').forEach(function(btn) {
        btn.addEventListener('click', async function() {
            const id = btn.getAttribute('data-id');
            await apiRequest('/workout-sets/' + id, 'DELETE');
            loadSets();
            loadVolume();
        });
    });
}

document.getElementById('add-set-btn').addEventListener('click', async function() {
    const exerciseId = document.getElementById('exercise-select').value;
    const reps = document.getElementById('reps-input').value;
    const weight = document.getElementById('weight-input').value;
    const errorEl = document.getElementById('set-error');

    errorEl.textContent = '';

    if (!exerciseId || !reps || !weight) {
        errorEl.textContent = 'Моля, попълнете всички полета.';
        return;
    }

    try {
        await apiRequest('/workout-sets', 'POST', {
            workout: { id: parseInt(workoutId) },
            exercise: { id: parseInt(exerciseId) },
            reps: parseInt(reps),
            weightKg: parseFloat(weight)
        });

        document.getElementById('reps-input').value = '';
        document.getElementById('weight-input').value = '';

        loadSets();
        loadVolume();
    } catch (err) {
        errorEl.textContent = err.message || 'Възникна грешка.';
    }
});

document.getElementById('show-new-exercise').addEventListener('click', function() {
    document.getElementById('new-exercise-form').classList.toggle('hidden');
});

document.getElementById('save-new-exercise-btn').addEventListener('click', async function() {
    const name = document.getElementById('new-exercise-name').value;
    const muscleGroup = document.getElementById('new-exercise-muscle').value;
    const errorEl = document.getElementById('set-error');

    errorEl.textContent = '';

    if (!name || !muscleGroup) {
        errorEl.textContent = 'Моля, попълнете име и мускулна група.';
        return;
    }

    try {
        await apiRequest('/exercises', 'POST', { name: name, muscleGroup: muscleGroup });
        document.getElementById('new-exercise-name').value = '';
        document.getElementById('new-exercise-muscle').value = '';
        document.getElementById('new-exercise-form').classList.add('hidden');
        loadExercises();
    } catch (err) {
        errorEl.textContent = err.message || 'Възникна грешка.';
    }
});

init();