document.addEventListener('DOMContentLoaded', async () => {
    await loadCoordinates();
    await loadLocations();
});

document.getElementById('add-person-form').addEventListener('submit', async (event) => {
    event.preventDefault();
    console.log("Add person form submitted");

    const name = document.getElementById('name').value;
    if (!name) {
        showNotification('Please fill the Name field.');
        return;
    }

    const birthday = document.getElementById('birthday').value;
    if (!birthday) {
        showNotification('Please fill the Birthday field.');
        return;
    }

    const coordinateID = document.getElementById('coordinateID').value;
    const coordinateX = document.getElementById('coordinateX').value;
    const coordinateY = document.getElementById('coordinateY').value;

    if ((!coordinateID && (!coordinateX || !coordinateY)) || (coordinateID && (coordinateX || coordinateY))) {
        showNotification('Please fill either Coordinate ID or both X and Y values.');
        return;
    }

    const locationID = document.getElementById('locationID').value;
    const locationX = document.getElementById('locationX').value;
    const locationY = document.getElementById('locationY').value;
    const locationZ = document.getElementById('locationZ').value;

    if ((!locationID && (!locationX || !locationY || !locationZ)) || (locationID && (locationX || locationY || locationZ))) {
        showNotification('Please fill either Location ID or all X, Y and Z values.');
        return;
    }

    const newPerson = {
        name: document.getElementById('name').value,
        coordinates: {
            id: coordinateID ? parseInt(coordinateID) : null,
            x: coordinateX ? parseFloat(coordinateX) : null,
            y: coordinateY ? parseFloat(coordinateY) : null
        },
        eyeColor: document.getElementById('eyeColor').value,
        hairColor: document.getElementById('hairColor').value,
        location: {
            id: locationID ? parseInt(locationID) : null,
            x: locationX ? parseFloat(locationX) : null,
            y: locationY ? parseFloat(locationY) : null,
            z: locationY ? parseFloat(locationZ) : null
        },
        height: parseFloat(document.getElementById('height').value),
        birthday: (new Date(document.getElementById('birthday').value)).toISOString(),
        nationality: document.getElementById('nationality').value,
    };

    try {
        const response = await apiCreatePerson(newPerson);
        if (response) {
            showNotification('Person added successfully');
        } else {
            showNotification('Failed to add person');
        }
    } catch (error) {
        showNotification(error.message);
    }

    await loadCoordinates();
    await loadLocations();
});

setInterval(
    async () => {
        await loadCoordinates();
        await loadLocations();
    },
    1000
)

async function loadCoordinates() {
    try {
        console.log("Loading coordinates");
        const coordinates = await apiGetCoordinates();
        const tbody = document.getElementById('coordinates-table').querySelector('tbody');
        tbody.innerHTML = ''; // Clear existing rows
        coordinates.forEach(coord => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${coord.id}</td>
                <td>${coord.x}</td>
                <td>${coord.y}</td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        showNotification('Failed to load coordinates');
    }
}

async function loadLocations() {
    try {
        const locations = await apiGetLocations();
        const tbody = document.getElementById('locations-table').querySelector('tbody');
        tbody.innerHTML = ''; // Clear existing rows
        locations.forEach(loc => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${loc.id}</td>
                <td>${loc.x}</td>
                <td>${loc.y}</td>
                <td>${loc.z}</td>
            `;
            tbody.appendChild(row);
        });
    } catch (error) {
        showNotification('Failed to load locations');
    }
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
