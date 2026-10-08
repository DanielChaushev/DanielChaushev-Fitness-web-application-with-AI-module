document.getElementById('show-register').addEventListener('click', function(e) {
    e.preventDefault();
    document.getElementById('login-card').classList.add('hidden');
    document.getElementById('register-card').classList.remove('hidden');
});

document.getElementById('show-login').addEventListener('click', function(e) {
    e.preventDefault();
    document.getElementById('register-card').classList.add('hidden');
    document.getElementById('login-card').classList.remove('hidden');
});

document.getElementById('login-btn').addEventListener('click', async function() {
    const email = document.getElementById('login-email').value;
    const password = document.getElementById('login-password').value;
    const errorEl = document.getElementById('login-error');

    errorEl.textContent = '';

    if (!email || !password) {
        errorEl.textContent = 'Моля, попълнете имейл и парола.';
        return;
    }

    try {
        const data = await apiRequest('/auth/login', 'POST', { email, password });
        setToken(data.token);
        window.location.href = 'dashboard.html';
    } catch (err) {
        errorEl.textContent = 'Грешен имейл или парола.';
    }
});

document.getElementById('register-btn').addEventListener('click', async function() {
    const firstName = document.getElementById('register-firstName').value;
    const lastName = document.getElementById('register-lastName').value;
    const email = document.getElementById('register-email').value;
    const password = document.getElementById('register-password').value;
    const gender = document.getElementById('register-gender').value;
    const age = document.getElementById('register-age').value;
    const height = document.getElementById('register-height').value;
    const weight = document.getElementById('register-weight').value;
    const goal = document.getElementById('register-goal').value;
    const activityLevel = document.getElementById('register-activity').value;
    const errorEl = document.getElementById('register-error');

    errorEl.textContent = '';

    if (!firstName || !lastName || !email || !password || !age || !height || !weight) {
        errorEl.textContent = 'Моля, попълнете всички полета.';
        return;
    }

    try {
        const data = await apiRequest('/auth/register', 'POST', {
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            gender: gender,
            age: parseInt(age),
            height: parseFloat(height),
            weight: parseFloat(weight),
            goal: goal,
            activityLevel: activityLevel
        });
        setToken(data.token);
        window.location.href = 'dashboard.html';
    } catch (err) {
        errorEl.textContent = err.message || 'Възникна грешка при регистрация.';
    }
});