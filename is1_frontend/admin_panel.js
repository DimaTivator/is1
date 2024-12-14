document.addEventListener('DOMContentLoaded', async function() {
    await loadPotentialAdmins();
});

async function loadPotentialAdmins() {
    console.log("Loading potential admins");
    const potential_admins = await apiGetPotentialAdmins();
    const tbody = document.getElementById('user-table').querySelector('tbody');
    tbody.innerHTML = ''; // Clear existing rows
    potential_admins.forEach(user => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.login}</td>
            <td>
                <button onclick="approveRequest(${user.id})">Approve</button>
            </td>
            <td>
                <button style="background-color: red;" onclick="denyRequest(${user.id})">Deny</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

async function approveRequest(id) {
    console.log("Approving request for user with id:", id);
    const response = await apiApproveAdminRequest(id);
    if (response.ok) {
        console.log("Request approved");
        await loadPotentialAdmins();
    } else {
        console.error("Failed to approve request");
    }
}

async function denyRequest(id) {
    console.log("Denying request for user with id:", id);
    const response = await apiDenyAdminRequest(id);
    if (response.ok) {
        console.log("Request denied");
        await loadPotentialAdmins();
    } else {
        console.error("Failed to deny request");
    }
}
