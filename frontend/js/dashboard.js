if (!isLoggedIn()) {
    window.location.href = 'index.html';
}

document.getElementById('logout-btn').addEventListener('click', function() {
    clearToken();
    window.location.href = 'index.html';
});

const ACTIVITY_LABELS = {
    'sedentary': 'Заседнал начин на живот',
    'light': 'Леко активен',
    'moderate': 'Умерено активен',
    'active': 'Активен',
    'very_active': 'Много активен'
};

function capitalize(text) {
    return text.charAt(0).toUpperCase() + text.slice(1);
}

async function loadDashboard() {
    const user = await getCurrentUser();
    if (!user) return;

    document.getElementById('greeting').textContent = 'Здравейте, ' + user.firstName;

    try {
        const bmiData = await apiRequest('/stats/bmi/' + user.id, 'GET');
        document.getElementById('bmi-value').textContent = bmiData.bmi;
        document.getElementById('bmi-category').textContent = bmiData.category;
        document.getElementById('calorie-needs').textContent = bmiData.dailyCalorieNeeds;
    } catch (err) {
        document.getElementById('bmi-category').textContent = 'Липсват данни за изчислението';
    }

    try {
        const trend = await apiRequest('/stats/weight-trend/' + user.id, 'GET');
        const sign = trend > 0 ? '+' : '';
        document.getElementById('weight-trend').textContent = sign + trend;
    } catch (err) {
        document.getElementById('weight-trend').textContent = 'Няма данни';
    }

    loadProfile(user);
}

async function loadProfile(user) {
    document.getElementById('p-height').textContent = user.height !== null ? user.height + ' см' : '—';
    document.getElementById('p-age').textContent = user.age !== null ? user.age + ' г.' : '—';
    document.getElementById('p-goal').textContent = user.goal ? capitalize(user.goal) : '—';
    document.getElementById('p-activity').textContent = ACTIVITY_LABELS[user.activityLevel] || '—';

    try {
        const logs = await apiRequest('/weight-logs/user/' + user.id, 'GET');
        if (logs.length > 0) {
            document.getElementById('p-weight').textContent = logs[logs.length - 1].weightKg + ' кг';
        } else if (user.weight !== null) {
            document.getElementById('p-weight').textContent = user.weight + ' кг';
        }
    } catch (err) {
        document.getElementById('p-weight').textContent = '—';
    }
}

loadDashboard();