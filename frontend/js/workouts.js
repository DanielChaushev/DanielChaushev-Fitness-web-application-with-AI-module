if (!isLoggedIn()) {
    window.location.href = 'index.html';
}

let currentUser = null;

async function loadWorkouts() {
    currentUser = await getCurrentUser();
    if (!currentUser) return;

    const workouts = await apiRequest('/workouts/user/' + currentUser.id, 'GET');
    renderWorkouts(workouts);
}

function renderWorkouts(workouts) {
    const listEl = document.getElementById('workouts-list');
    listEl.innerHTML = '';

    if (workouts.length === 0) {
        listEl.innerHTML =
            '<div class="card empty-state">' +
                '<i class="ti ti-barbell" aria-hidden="true"></i>' +
                '<p>Все още нямате добавени тренировки</p>' +
            '</div>';
        return;
    }

    workouts.forEach(function(workout) {
        const card = document.createElement('div');
        card.className = 'card row-between';
        card.innerHTML =
            '<div>' +
                '<p class="numeric item-sub">' + workout.date + '</p>' +
                '<p class="item-title">' + (workout.notes || 'Без бележка') + '</p>' +
            '</div>' +
            '<div class="row-actions">' +
                '<a href="workout-detail.html?id=' + workout.id + '" class="btn-outline">Преглед</a>' +
                '<button class="delete-btn btn-danger" data-id="' + workout.id + '">Изтрий</button>' +
            '</div>';
        listEl.appendChild(card);
    });

    document.querySelectorAll('.delete-btn').forEach(function(btn) {
        btn.addEventListener('click', async function() {
            const id = btn.getAttribute('data-id');
            await apiRequest('/workouts/' + id, 'DELETE');
            loadWorkouts();
        });
    });
}

document.getElementById('add-workout-btn').addEventListener('click', async function() {
    const date = document.getElementById('workout-date').value;
    const notes = document.getElementById('workout-notes').value;
    const errorEl = document.getElementById('workout-error');

    errorEl.textContent = '';

    if (!date) {
        errorEl.textContent = 'Моля, изберете дата.';
        return;
    }

    try {
        await apiRequest('/workouts', 'POST', {
            date: date,
            notes: notes,
            user: { id: currentUser.id }
        });
        document.getElementById('workout-notes').value = '';
        loadWorkouts();
    } catch (err) {
        errorEl.textContent = err.message || 'Възникна грешка.';
    }
});

loadWorkouts();