const loginForm = document.getElementById('login-form');
const registerForm = document.getElementById('register-form');
const toggleAuthBtn = document.getElementById('toggle-auth');
const formTitle = document.getElementById('form-title');

function showNotification(message) {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.classList.add('show');

    // Hide notification after 3 seconds
    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

// Toggle between login and registration forms
toggleAuthBtn.addEventListener('click', () => {
    if (loginForm.classList.contains('hidden')) {
        registerForm.style.display = 'none';
        loginForm.style.display = '';
        loginForm.classList.remove('hidden');
        registerForm.classList.add('hidden');
        toggleAuthBtn.textContent = "Don't have an account? Register";
        formTitle.textContent = "Login";
    } else {
        loginForm.style.display = 'none';
        registerForm.style.display = '';
        loginForm.classList.add('hidden');
        registerForm.classList.remove('hidden');
        toggleAuthBtn.textContent = "Already have an account? Login";
        formTitle.textContent = "Register";
    }
});

// Handle login submission
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('login-username').value;
    if (!username) {
        showNotification('Username cannot be empty');
        return;
    }

    const password = document.getElementById('login-password').value;
    if (!password) {
        showNotification('Password cannot be empty');
        return
    }

    try {
        const response = await fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                login: username,
                password: password
            })
        });

        if (!response.ok) {
            throw new Error('Invalid login credentials');
        }

        const {token} = await response.json();
        localStorage.setItem('token', token);
        window.location.href = 'main.html';
    } catch (error) {
        showNotification(error.message);
    }
});

// Handle registration submission
registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('register-username').value;
    if (!username) {
        showNotification('Username cannot be empty');
        return
    }

    const password = document.getElementById('register-password').value;
    if (!password) {
        showNotification('Password cannot be empty');
        return
    }
    if (password.length <= 2) {
        showNotification('Password must be at least 3 characters long');
        return
    }

    try {
        const response = await fetch('http://localhost:8080/register', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({
                login: username,
                password: password
            })
        });

        if (!response.ok) {
            throw new Error('Login already exists');
        }

        showNotification("Registration successful! Please log in.");
        toggleAuthBtn.click(); // Switch to login form
    } catch (error) {
        showNotification(error.message);
    }
});
