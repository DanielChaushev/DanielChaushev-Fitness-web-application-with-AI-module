if (!isLoggedIn()) {
    window.location.href = 'index.html';
}

let currentUser = null;
let chartInstance = null;

function getToday() {
    return new Date().toISOString().split('T')[0];
}

async function init() {
    currentUser = await getCurrentUser();
    if (!currentUser) return;

    loadTrend();
    loadWeightLogs();
}

async function loadTrend() {
    const trendEl = document.getElementById('trend-value');
    try {
        const trend = await apiRequest('/stats/weight-trend/' + currentUser.id, 'GET');
        const sign = trend > 0 ? '+' : '';
        trendEl.className = 'numeric stat-value';
        trendEl.textContent = sign + trend;
    } catch (err) {
        trendEl.className = 'stat-sub';
        trendEl.textContent = 'Нужни са поне 2 записа с различни дати';
    }
}

async function loadWeightLogs() {
    const logs = await apiRequest('/weight-logs/user/' + currentUser.id, 'GET');
    renderChart(logs);
    renderLogsList(logs);
}

function renderChart(logs) {
    const ctx = document.getElementById('weight-chart');

    const years = logs.map(function(log) { return log.date.split('-')[0]; });
    const uniqueYears = [...new Set(years)];
    const showYear = uniqueYears.length > 1;

    const labels = logs.map(function(log) {
        const parts = log.date.split('-');
        if (showYear) {
            return parts[2] + '.' + parts[1] + '.' + parts[0].slice(2);
        }
        return parts[2] + '.' + parts[1];
    });
    const data = logs.map(function(log) { return log.weightKg; });

    if (chartInstance) {
        chartInstance.destroy();
    }

    chartInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Тегло (кг)',
                data: data,
                borderColor: '#4C7BDE',
                backgroundColor: 'rgba(76, 123, 222, 0.1)',
                tension: 0.3,
                fill: true
            }]
        },
        options: {
            responsive: true,
            layout: {
                padding: {
                    left: 10,
                    right: 20
                }
            },
            plugins: {
                legend: { display: false }
            },
            scales: {
                y: { ticks: { color: '#9A9CA3' }, grid: { color: '#37383F' } },
                x: {
                    ticks: {
                        color: '#9A9CA3',
                        maxRotation: 0,
                        autoSkip: false,
                        maxTicksLimit: 6
                    },
                    grid: { color: '#37383F' }
                }
            }
        }
    });
}

function renderLogsList(logs) {
    const listEl = document.getElementById('logs-list');
    listEl.innerHTML = '';

    const reversedLogs = logs.slice().reverse();

    reversedLogs.forEach(function(log) {
        const card = document.createElement('div');
        card.className = 'card row-between';
        card.innerHTML =
            '<div>' +
                '<p class="numeric log-weight">' + log.weightKg + ' кг</p>' +
                '<p class="item-sub">' + log.date + '</p>' +
            '</div>' +
            '<button class="delete-log-btn btn-danger" data-id="' + log.id + '">Изтрий</button>';
        listEl.appendChild(card);
    });

    document.querySelectorAll('.delete-log-btn').forEach(function(btn) {
        btn.addEventListener('click', async function() {
            const id = btn.getAttribute('data-id');
            await apiRequest('/weight-logs/' + id, 'DELETE');
            loadTrend();
            loadWeightLogs();
        });
    });
}

document.getElementById('add-weight-btn').addEventListener('click', async function() {
    const weight = document.getElementById('weight-input').value;
    const errorEl = document.getElementById('weight-error');

    errorEl.textContent = '';

    if (!weight) {
        errorEl.textContent = 'Моля, въведете тегло.';
        return;
    }

    try {
        await apiRequest('/weight-logs', 'POST', {
            date: getToday(),
            weightKg: parseFloat(weight),
            user: { id: currentUser.id }
        });

        document.getElementById('weight-input').value = '';
        loadTrend();
        loadWeightLogs();
    } catch (err) {
        errorEl.textContent = err.message || 'Възникна грешка.';
    }
});

init();