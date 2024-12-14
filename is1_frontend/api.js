const API_URL = 'http://localhost:43476';
// const API_URL = 'http://localhost:8080';

async function apiGetPersons(page, filters){
    const token = localStorage.getItem('token');

    const params = new URLSearchParams({ page: page });
    for (const key in filters) {
        if (filters[key] !== null) {
            params.append(key, filters[key]);
        }
    }
    console.log(params.toString());

    try {
        const response = await fetch(`${API_URL}/persons?${params.toString()}`, {
            headers: {'Authorization': `Bearer ${token}`}
        });
        if (!response.ok) {
            showNotification('Failed to load people');
            return [];
        }
        return await response.json();
    } catch (error) {
        showNotification('Server is not responding');
        return [];
    }
}

async function apiGetCoordinates() {
    const token = localStorage.getItem('token');
    try {
        console.log("Fetching coordinates");
        const response = await fetch(`${API_URL}/coordinates`, {
            headers: {'Authorization': `Bearer ${token}`}
        });
        console.log(response);
        if (!response.ok) {
            showNotification('Failed to load coordinates');
            return [];
        }
        return await response.json();
    } catch (error) {
        showNotification('Server is not responding');
        return [];
    }
}

async function apiGetLocations() {
    const token = localStorage.getItem('token');
    try {
        console.log("Fetching locations");
        const response = await fetch(`${API_URL}/locations`, {
            headers: {'Authorization': `Bearer ${token}`}
        });
        console.log(response);
        if (!response.ok) {
            showNotification('Failed to load locations');
            return [];
        }
        return await response.json();
    } catch (error) {
        showNotification('Server is not responding');
        return [];
    }
}

async function apiDeletePerson(id) {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/persons/id?id=${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            const errorMessage = await response.text();
            showNotification(`Permission denied`);
            return null;
        }

        return true;
    } catch (error) {
        showNotification("An error occurred while trying to delete the person.");
    }
}

async function apiCreatePerson(personData) {
    const token = localStorage.getItem('token');

    try {
        const response = await fetch(`${API_URL}/persons`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(personData),
        });

        if (response.ok) {
            return await response.json();
        }
        if (response.status === 400) {
            throw new Error('Coordinates or location already exist');
        }
        if (response.status === 404) {
            throw new Error('ID not found');
        }

    } catch (error) {
        console.error('Error creating person:', error);
        throw error;
    }
}

async function apiUpdatePerson(personData) {
    const token = localStorage.getItem('token');

    try {
        const response = await fetch(`${API_URL}/persons`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(personData),
        });

        if (response.ok) {
            return await response.json();
        }
        if (response.status === 400) {
            throw new Error('Coordinates or location already exist');
        }
        if (response.status === 404) {
            throw new Error('ID not found');
        }

    } catch (error) {
        console.error('Error updating person:', error);
        throw error;
    }
}

async function apiCountWithNationalityEqualTo(country) {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/persons/count-nationality-equal?country=${country}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        console.log(response);
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error('Error counting persons');
        throw error;
    }
}

async function apiCountWithNationalityLessThan(country) {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/persons/count-nationality-less?country=${country}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        console.log(response);
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error('Error counting persons');
        throw error;
    }
}

async function apiCountWithEyeColor(color) {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/persons/count-eye-color?color=${color}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        console.log(response);
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error('Error counting persons');
        throw error;
    }
}

async function apiCountWithHairColor(color) {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/persons/count-hair-color?color=${color}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        console.log(response);
        if (response.ok) {
            return await response.json();
        }
    } catch (error) {
        console.error('Error counting persons');
        throw error;
    }
}

async function apiDeleteByHeight(height) {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/persons/height?height=${height}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
    } catch (error) {
        console.error('Error deleting persons');
    }
}

async function apiRequestAdmin() {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/request/admin`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        console.log(response);
        return await response;
    } catch (error) {
        console.error('Error requesting admin');
    }
}

async function apiGetPotentialAdmins() {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/admin`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        console.log(response);
        if (response.ok) {
            return await response.json();
        } else {
            throw Error("Permission denied");
        }
    } catch (error) {
        console.error('Error getting potential admins');
        throw error;
    }
}

async function apiApproveAdminRequest(id) {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/admin/${id}:admin`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        console.log(response);
        return response;
    } catch (error) {
        console.error('Error approving admin request');
    }
}

async function apiDenyAdminRequest(id) {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${API_URL}/admin/${id}:user`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });
        console.log(response);
        return response;
    } catch (error) {
        console.error('Error denying admin request');
    }
}
