const API_BASE_URL = 'http://localhost:8080/api';

function getToken() {
    return localStorage.getItem('jwt_token');
}

function setToken(token) {
    localStorage.setItem('jwt_token', token);
}

function clearToken() {
    localStorage.removeItem('jwt_token');
}

function isLoggedIn() {
    return getToken() !== null;
}

async function apiRequest(endpoint, method = 'GET', body = null) {
    const headers = {
        'Content-Type': 'application/json'
    };

    const token = getToken();
    if (token) {
        headers['Authorization'] = 'Bearer ' + token;
    }

    const options = {
        method: method,
        headers: headers
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(API_BASE_URL + endpoint, options);

    if (response.status === 401 || response.status === 403) {
        clearToken();
        window.location.href = 'index.html';
        return null;
    }

    if (!response.ok) {
        const errorData = await response.json().catch(() => ({ error: 'Възникна грешка.' }));
        throw new Error(errorData.error || 'Възникна грешка.');
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

function parseJwt(token) {
    const payload = token.split('.')[1];
    const decoded = decodeURIComponent(
        atob(payload).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join('')
    );
    return JSON.parse(decoded);
}

async function getCurrentUser() {
    const token = getToken();
    if (!token) return null;

    const payload = parseJwt(token);
    const email = payload.sub;

    const users = await apiRequest('/users', 'GET');
    return users.find(function(u) { return u.email === email; });
}