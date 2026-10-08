if (!isLoggedIn()) {
    window.location.href = 'index.html';
}

let currentUser = null;

async function init() {
    currentUser = await getCurrentUser();
    if (!currentUser) return;

    loadPlans();
}

async function loadPlans() {
    const plans = await apiRequest('/ai/user/' + currentUser.id, 'GET');
    renderPlans(plans);
}

function renderPlans(plans) {
    const listEl = document.getElementById('plans-list');
    listEl.innerHTML = '';

    if (plans.length === 0) {
        listEl.innerHTML =
            '<div class="card empty-state">' +
                '<i class="ti ti-sparkles" aria-hidden="true"></i>' +
                '<p>Все още нямате генерирани планове</p>' +
                '<p class="empty-sub">Изберете брой дни и натиснете бутона.</p>' +
            '</div>';
        return;
    }

    const reversedPlans = plans.slice().reverse();
    const latest = reversedPlans[0];
    const older = reversedPlans.slice(1);

    const latestCard = document.createElement('div');
    latestCard.className = 'card';
    latestCard.innerHTML =
        '<div class="row-between plan-head">' +
            '<div>' +
                '<span class="badge">Седмичен план</span>' +
                '<p class="item-sub">' + new Date(latest.generatedAt).toLocaleString('bg-BG') + '</p>' +
            '</div>' +
            '<button class="delete-plan-btn btn-danger" data-id="' + latest.id + '">Изтрий</button>' +
        '</div>' +
        '<div class="ai-plan-content">' + marked.parse(latest.content) + '</div>';
    listEl.appendChild(latestCard);

    if (older.length > 0) {
        const toggle = document.createElement('p');
        toggle.className = 'link toggle-link';
        toggle.textContent = 'Покажи по-стари планове (' + older.length + ')';
        listEl.appendChild(toggle);

        const olderContainer = document.createElement('div');
        olderContainer.className = 'hidden';
        listEl.appendChild(olderContainer);

        toggle.addEventListener('click', function() {
            const isHidden = olderContainer.classList.toggle('hidden');
            toggle.textContent = isHidden
                ? 'Покажи по-стари планове (' + older.length + ')'
                : 'Скрий по-стари планове';
        });

        older.forEach(function(plan) {
            const row = document.createElement('div');
            row.className = 'card card-compact row-between';
            row.innerHTML =
                '<div>' +
                    '<span class="badge">Седмичен план</span>' +
                    '<p class="item-sub">' + new Date(plan.generatedAt).toLocaleString('bg-BG') + '</p>' +
                '</div>' +
                '<div class="row-actions">' +
                    '<button class="view-plan-btn btn-outline">Виж</button>' +
                    '<button class="delete-plan-btn btn-danger" data-id="' + plan.id + '">Изтрий</button>' +
                '</div>';
            olderContainer.appendChild(row);

            const contentEl = document.createElement('div');
            contentEl.className = 'card ai-plan-content hidden';
            contentEl.innerHTML = marked.parse(plan.content);
            olderContainer.appendChild(contentEl);

            row.querySelector('.view-plan-btn').addEventListener('click', function() {
                contentEl.classList.toggle('hidden');
            });
        });
    }

    document.querySelectorAll('.delete-plan-btn').forEach(function(btn) {
        btn.addEventListener('click', async function() {
            const id = btn.getAttribute('data-id');
            await apiRequest('/ai/' + id, 'DELETE');
            loadPlans();
        });
    });
}

document.getElementById('generate-btn').addEventListener('click', async function() {
    const btn = document.getElementById('generate-btn');
    const errorEl = document.getElementById('ai-error');
    const daysPerWeek = parseInt(document.getElementById('days-select').value);

    errorEl.textContent = '';
    btn.disabled = true;
    btn.textContent = 'Генериране...';

    try {
        await apiRequest('/ai/generate/' + currentUser.id, 'POST', {
            daysPerWeek: daysPerWeek
        });
        loadPlans();
    } catch (err) {
        errorEl.textContent = err.message || 'Възникна грешка при генериране.';
    } finally {
        btn.disabled = false;
        btn.textContent = 'Генерирайте нов план';
    }
});

init();