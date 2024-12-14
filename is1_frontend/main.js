document.addEventListener('DOMContentLoaded', async () => {
    document.getElementById('logout-btn').addEventListener('click', () => {
        localStorage.removeItem('token');
        window.location.href = 'index.html';
    });

    // document.getElementById('add-person').addEventListener('click', () => {
    //     window.open('add_person.html', 'Add Person', 'width=400,height=600');
    // });

    const add_dialog = document.getElementById('addPersonDialog');

    document.getElementById('add-person').addEventListener('click', () => {
        add_dialog.showModal();
    });

    document.getElementById('closeAddDialog').addEventListener('click', () => {
        add_dialog.close();
    });

    document.getElementById('admin-panel-btn').addEventListener('click', () => {
        window.open('admin_panel.html', 'Admin Panel', 'width=400,height=600');
    });

    document.getElementById('show-imports').addEventListener('click', async () => {
        document.getElementById('importHistoryDialog').showModal();
    });
    
    document.getElementById('closeImportHistoryDialog').addEventListener('click', () => {
        document.getElementById('importHistoryDialog').close();
    });

    document.getElementById('count-nationality-btn').addEventListener('click', countWithNationalityEqualTo);
    document.getElementById('count-with-nationality-less-btn').addEventListener('click', countWithNationalityLessThan);
    document.getElementById('count-eye-color-btn').addEventListener('click', countWithEyeColorEqualTo);
    document.getElementById('count-hair-color-btn').addEventListener('click', countWithHairColorEqualTo);
    document.getElementById('remove-by-height-btn').addEventListener('click', deleteByHeight);

    document.getElementById('request-admin-btn').addEventListener('click', requestAdmin);

    document.getElementById('prev-page').addEventListener('click', previousPage);
    document.getElementById('next-page').addEventListener('click', nextPage);

    document.getElementById('filter-button').addEventListener('click', filterTable);

    Array.from(document.getElementsByClassName('sort-button')).forEach(button => {
        button.addEventListener('click', () => {
            sort_column = button.value;
            if (button.textContent === '<') {
                button.textContent = '>';
            } else {
                button.textContent = '<';
            }
            sortTable();
        });
    });

    await filterTable();
});

// setInterval(
//     async () => {
//         await loadPersons();
//     },
//     2000
// )

let persons = [];
let page = 0;
let page_size = 5;
let filters = {};
let sort_column = null;
let ascending = null;

const edit_dialog = document.getElementById('editPersonDialog');

document.getElementById('closeEditDialog').addEventListener('click', () => {
    edit_dialog.close();
});

async function loadPersons() {
    try {
        persons = await apiGetPersons(page, filters);
        const tbody = document.getElementById('person-table').querySelector('tbody');
        tbody.innerHTML = ''; // Clear existing rows
        persons.forEach(person => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${person.id}</td>
                <td>${person.name}</td>
                <td>${person.coordinates ? `(${person.coordinates.x}, ${person.coordinates.y})` : 'N/A'}</td>
                <td>${person.creationDate ? new Date(person.creationDate).toLocaleString() : 'N/A'}</td>
                <td>${person.eyeColor}</td>
                <td>${person.hairColor}</td>
                <td>${person.location ? `(${person.location.x}, ${person.location.y}, ${person.location.z})` : 'N/A'}</td>
                <td>${person.height}</td>
                <td>${new Date(person.birthday).toLocaleDateString()}</td>
                <td>${person.nationality || 'N/A'}</td>
                <td>${person.user ? person.user.login : 'N/A'}</td>
                <td>
                    <button onclick="editPerson(${person.id})">Edit</button>
                </td>
                <td>  
                    <button onclick="deletePerson(${person.id})" style="background-color: #e53935">Delete</button>
                </td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        showNotification('Failed to load people');
    }
}

function getTimestampFromDate(dateString) {
    if (dateString.length === 0) {
        return dateString;
    }
    try {
        const [day, month, year] = dateString.split('/').map(Number);
        const date = new Date(Date.UTC(year, month - 1, day));
        return date.getTime();
    } catch (error) {
        showNotification('Invalid date format');
        return null;
    }
}

function getTimestampFromDateTime(dateTimeString) {
    if (dateTimeString.length === 0) {
        return dateTimeString;
    }
    try {
        const [datePart, timePart] = dateTimeString.split(', ');
        const [day, month, year] = datePart.split('/').map(Number);
        const [hours, minutes, seconds] = timePart.split(':').map(Number);
        const date = new Date(Date.UTC(year, month - 1, day, hours, minutes, seconds));
        return date.getTime();
    } catch (error) {
        showNotification('Invalid date-time format');
        return null;
    }
}

async function sortTable() {
    if (ascending === null) {
        ascending = true;
    } else {
        ascending = !ascending;
    }
    await filterTable();
}

async function filterTable() {
    console.log('Filter clicked');
    page = 0;
    filters = {
        id: document.getElementById('filter-id').value.toLowerCase(),
        name: document.getElementById('filter-name').value.toLowerCase(),
        coordinates: document.getElementById('filter-coordinates').value.toLowerCase(),
        creationDate: getTimestampFromDate(document.getElementById('filter-creation-date').value.toLowerCase()),
        eyeColor: document.getElementById('filter-eye-color').value.toLowerCase(),
        hairColor: document.getElementById('filter-hair-color').value.toLowerCase(),
        location: document.getElementById('filter-location').value.toLowerCase(),
        height: document.getElementById('filter-height').value.toLowerCase(),
        birthday: getTimestampFromDate(document.getElementById('filter-birthday').value.toLowerCase()),
        nationality: document.getElementById('filter-nationality').value.toLowerCase(),
        login: document.getElementById('filter-user').value.toLowerCase(),
        sortBy: sort_column,
        ascending: ascending
    };
    for (const key in filters) {
        if (filters[key] === '') {
            filters[key] = null;
        }
    }
    console.log(filters);
    await loadPersons();
}


function showImports() {
    
}


function editPerson(id) {
    console.log('Edit person clicked:', id);
    const person = persons.find(p => p.id === id);
    localStorage.setItem('editPerson', JSON.stringify(person));

    const iframe = document.getElementById('editIframe');
    iframe.src = `edit_person.html`;
    document.getElementById('editPersonDialog').showModal();
}

async function deletePerson(id) {
    const result = await apiDeletePerson(id);
    if (result) {
        showNotification('Person deleted successfully');
        await loadPersons();
    }
}

async function countWithNationalityEqualTo() {
    const country = document.getElementById("nationality-eq").value;
    const count = await apiCountWithNationalityEqualTo(country);
    showNotification(`Count: ${count}`);
}

async function countWithNationalityLessThan() {
    const country = document.getElementById("nationality-less").value;
    const count = await apiCountWithNationalityLessThan(country);
    showNotification(`Count: ${count}`);
}

async function countWithEyeColorEqualTo() {
    const color = document.getElementById("eyeColor").value;
    const count = await apiCountWithEyeColor(color);
    showNotification(`Count: ${count}`);
}

async function countWithHairColorEqualTo() {
    const color = document.getElementById("hairColor").value;
    const count = await apiCountWithHairColor(color);
    showNotification(`Count: ${count}`);
}

async function deleteByHeight() {
    const height = parseFloat(document.getElementById("height-input").value);
    const result = await apiDeleteByHeight(height);
    showNotification('Deleted successfully');
    await loadPersons();
}

async function requestAdmin() {
    const result = await apiRequestAdmin();
    if (result.status === 202) {
        showNotification('Request sent');
    } else if (result.status === 200) {
        showNotification('You are admin');
    } else {
        showNotification('Request failed');
    }
}

async function nextPage() {
    const nextPagePersons = await apiGetPersons(page + 1, filters);
    if (nextPagePersons.length > 0) {
        page++;
        await loadPersons();
    } else {
        showNotification('No more pages');
    }
}

async function previousPage() {
    if (page === 0) {
        return;
    }
    page--;
    await loadPersons();
}

function openModal(url) {
    fetch(url)
        .then(response => response.text())
        .then(html => {
            document.getElementById('modal-body').innerHTML = html;
            document.getElementById('modal').style.display = 'block';
        })
        .catch(error => {
            console.error('Error loading modal content:', error);
        });
}

function closeModal() {
    document.getElementById('modal').style.display = 'none';
}

function showNotification(message) {
    const notification = document.getElementById('notification');
    notification.textContent = message;
    notification.classList.add('show');

    // Hide notification after 3 seconds
    setTimeout(() => {
        notification.classList.remove('show');
    }, 3000);
}

