const API_BASE = '/api';

// --- State ---
let currentView = 'dashboard';

// --- DOM Elements ---
const contentArea = document.getElementById('content-area');
const pageTitle = document.getElementById('page-title');
const navItems = document.querySelectorAll('.nav-item');
const modalOverlay = document.getElementById('modal-container');
const modalTitle = document.getElementById('modal-title');
const modalBody = document.getElementById('modal-body');
const closeModalBtn = document.getElementById('close-modal');
const toast = document.getElementById('toast');
const toastMessage = document.getElementById('toast-message');

// --- Initialization ---
document.addEventListener('DOMContentLoaded', () => {
    loadView('dashboard');

    navItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();
            navItems.forEach(nav => nav.classList.remove('active'));
            item.classList.add('active');
            const target = item.getAttribute('data-target');
            loadView(target);
        });
    });

    closeModalBtn.addEventListener('click', closeModal);
});

// --- Routing / View Logic ---
async function loadView(view) {
    currentView = view;
    contentArea.innerHTML = '<div style="text-align:center; padding:50px;"><i class="fa-solid fa-spinner fa-spin fa-3x" style="color:var(--primary)"></i></div>';
    
    try {
        switch(view) {
            case 'dashboard':
                pageTitle.textContent = 'Dashboard Overview';
                await renderDashboard();
                break;
            case 'donors':
                pageTitle.textContent = 'Manage Donors';
                await renderDonors();
                break;
            case 'drives':
                pageTitle.textContent = 'Manage Drives';
                await renderDrives();
                break;
            case 'recipients':
                pageTitle.textContent = 'Manage Recipients';
                await renderRecipients();
                break;
            case 'items':
                pageTitle.textContent = 'Donated Items';
                await renderItems();
                break;
        }
    } catch (error) {
        showToast(error.message, true);
        contentArea.innerHTML = `<div class="table-container" style="color:red;">Error loading data: ${error.message}</div>`;
    }
}

// --- Dashboard ---
async function renderDashboard() {
    const itemsRes = await fetch(`${API_BASE}/items`);
    const items = await itemsRes.json();
    
    const donorsRes = await fetch(`${API_BASE}/donors`);
    const donors = await donorsRes.json();

    const distributed = items.filter(i => i.distributed).length;
    const stock = items.length - distributed;

    const html = `
        <div class="stats-grid">
            <div class="stat-card glass-effect">
                <div class="stat-icon bg-purple"><i class="fa-solid fa-shirt"></i></div>
                <div class="stat-info">
                    <h3>Total Items</h3>
                    <p>${items.length}</p>
                </div>
            </div>
            <div class="stat-card glass-effect">
                <div class="stat-icon bg-teal"><i class="fa-solid fa-users"></i></div>
                <div class="stat-info">
                    <h3>Total Donors</h3>
                    <p>${donors.length}</p>
                </div>
            </div>
            <div class="stat-card glass-effect">
                <div class="stat-icon bg-pink"><i class="fa-solid fa-hand-holding-heart"></i></div>
                <div class="stat-info">
                    <h3>Distributed</h3>
                    <p>${distributed}</p>
                </div>
            </div>
            <div class="stat-card glass-effect">
                <div class="stat-icon bg-orange"><i class="fa-solid fa-box-open"></i></div>
                <div class="stat-info">
                    <h3>In Stock</h3>
                    <p>${stock}</p>
                </div>
            </div>
        </div>
        <div class="table-container">
            <h3 style="margin-bottom: 20px;">Recent Donations</h3>
            <table>
                <thead>
                    <tr>
                        <th>Item</th>
                        <th>Condition</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    ${items.slice(0, 5).map(item => `
                        <tr>
                            <td>${item.description}</td>
                            <td><span class="badge badge-${item.itemCondition.toLowerCase()}">${item.itemCondition}</span></td>
                            <td>
                                ${item.distributed 
                                    ? '<span class="badge badge-success">Distributed</span>' 
                                    : '<span class="badge badge-warning">In Stock</span>'}
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
    contentArea.innerHTML = html;
}

// --- Donors ---
async function renderDonors() {
    const res = await fetch(`${API_BASE}/donors`);
    const donors = await res.json();

    let html = `
        <div class="table-container">
            <div class="table-header">
                <h3>Donors List</h3>
                <button class="btn btn-primary" onclick="openDonorForm()"><i class="fa-solid fa-plus"></i> Add Donor</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${donors.map(d => `
                        <tr>
                            <td>#${d.id}</td>
                            <td><strong>${d.name}</strong></td>
                            <td>${d.email || '-'}</td>
                            <td>${d.phone || '-'}</td>
                            <td class="actions-cell">
                                <button class="btn btn-sm btn-edit" onclick="openDonorForm(${d.id})"><i class="fa-solid fa-pen"></i></button>
                                <button class="btn btn-sm btn-danger" onclick="deleteDonor(${d.id})"><i class="fa-solid fa-trash"></i></button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
    contentArea.innerHTML = html;
}

function openDonorForm(id = null) {
    const isEdit = id !== null;
    modalTitle.textContent = isEdit ? 'Edit Donor' : 'Add New Donor';
    modalBody.innerHTML = `
        <form id="addForm" onsubmit="submitDonor(event, ${id})">
            <div class="form-group">
                <label>Name</label>
                <input type="text" class="form-control" id="d_name" required>
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="email" class="form-control" id="d_email">
            </div>
            <div class="form-group">
                <label>Phone</label>
                <input type="text" class="form-control" id="d_phone">
            </div>
            <div class="form-actions">
                <button type="button" class="btn btn-edit" onclick="closeModal()">Cancel</button>
                <button type="submit" class="btn btn-primary">${isEdit ? 'Update' : 'Save'} Donor</button>
            </div>
        </form>
    `;
    
    if (isEdit) {
        // Fetch existing data
        fetch(`${API_BASE}/donors/${id}`).then(res => res.json()).then(data => {
            document.getElementById('d_name').value = data.name;
            document.getElementById('d_email').value = data.email || '';
            document.getElementById('d_phone').value = data.phone || '';
            openModal();
        });
    } else {
        openModal();
    }
}

async function submitDonor(e, id) {
    e.preventDefault();
    const payload = {
        name: document.getElementById('d_name').value,
        email: document.getElementById('d_email').value,
        phone: document.getElementById('d_phone').value
    };
    
    if (id) {
        await apiCall(`${API_BASE}/donors/${id}`, 'PUT', payload, 'Donor updated successfully');
    } else {
        await apiCall(`${API_BASE}/donors`, 'POST', payload, 'Donor added successfully');
    }
    closeModal();
    loadView('donors');
}

async function deleteDonor(id) {
    if(confirm('Are you sure you want to delete this donor?')) {
        await apiCall(`${API_BASE}/donors/${id}`, 'DELETE', null, 'Donor deleted');
        loadView('donors');
    }
}

// --- Drives ---
async function renderDrives() {
    const res = await fetch(`${API_BASE}/drives`);
    const drives = await res.json();

    let html = `
        <div class="table-container">
            <div class="table-header">
                <h3>Donation Drives</h3>
                <button class="btn btn-primary" onclick="openDriveForm()"><i class="fa-solid fa-plus"></i> Add Drive</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${drives.map(d => `
                        <tr>
                            <td>#${d.id}</td>
                            <td><strong>${d.name}</strong></td>
                            <td>${d.startDate}</td>
                            <td>${d.endDate}</td>
                            <td class="actions-cell">
                                <button class="btn btn-sm btn-danger" onclick="deleteDrive(${d.id})"><i class="fa-solid fa-trash"></i></button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
    contentArea.innerHTML = html;
}

function openDriveForm() {
    modalTitle.textContent = 'Create Drive';
    modalBody.innerHTML = `
        <form onsubmit="submitDrive(event)">
            <div class="form-group">
                <label>Drive Name</label>
                <input type="text" class="form-control" id="dr_name" required>
            </div>
            <div class="form-group">
                <label>Start Date</label>
                <input type="date" class="form-control" id="dr_start" required>
            </div>
            <div class="form-group">
                <label>End Date</label>
                <input type="date" class="form-control" id="dr_end" required>
            </div>
            <div class="form-actions">
                <button type="button" class="btn btn-edit" onclick="closeModal()">Cancel</button>
                <button type="submit" class="btn btn-primary">Save Drive</button>
            </div>
        </form>
    `;
    openModal();
}

async function submitDrive(e) {
    e.preventDefault();
    const payload = {
        name: document.getElementById('dr_name').value,
        startDate: document.getElementById('dr_start').value,
        endDate: document.getElementById('dr_end').value
    };
    await apiCall(`${API_BASE}/drives`, 'POST', payload, 'Drive created successfully');
    closeModal();
    loadView('drives');
}

async function deleteDrive(id) {
    if(confirm('Delete this drive?')) {
        await apiCall(`${API_BASE}/drives/${id}`, 'DELETE', null, 'Drive deleted');
        loadView('drives');
    }
}


// --- Recipients ---
async function renderRecipients() {
    const res = await fetch(`${API_BASE}/recipients`);
    const data = await res.json();

    let html = `
        <div class="table-container">
            <div class="table-header">
                <h3>Recipients (NGOs/Orphanages)</h3>
                <button class="btn btn-primary" onclick="openRecipientForm()"><i class="fa-solid fa-plus"></i> Add Recipient</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Org Name</th>
                        <th>Contact Person</th>
                        <th>Address</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${data.map(d => `
                        <tr>
                            <td><strong>${d.organizationName}</strong></td>
                            <td>${d.contactPerson || '-'}</td>
                            <td>${d.address || '-'}</td>
                            <td class="actions-cell">
                                <button class="btn btn-sm btn-danger" onclick="deleteRecipient(${d.id})"><i class="fa-solid fa-trash"></i></button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
    contentArea.innerHTML = html;
}

function openRecipientForm() {
    modalTitle.textContent = 'Add Recipient';
    modalBody.innerHTML = `
        <form onsubmit="submitRecipient(event)">
            <div class="form-group">
                <label>Organization Name</label>
                <input type="text" class="form-control" id="r_name" required>
            </div>
            <div class="form-group">
                <label>Contact Person</label>
                <input type="text" class="form-control" id="r_contact">
            </div>
            <div class="form-group">
                <label>Address</label>
                <input type="text" class="form-control" id="r_address">
            </div>
            <div class="form-actions">
                <button type="button" class="btn btn-edit" onclick="closeModal()">Cancel</button>
                <button type="submit" class="btn btn-primary">Save Recipient</button>
            </div>
        </form>
    `;
    openModal();
}

async function submitRecipient(e) {
    e.preventDefault();
    const payload = {
        organizationName: document.getElementById('r_name').value,
        contactPerson: document.getElementById('r_contact').value,
        address: document.getElementById('r_address').value
    };
    await apiCall(`${API_BASE}/recipients`, 'POST', payload, 'Recipient added');
    closeModal();
    loadView('recipients');
}

async function deleteRecipient(id) {
    if(confirm('Delete this recipient?')) {
        await apiCall(`${API_BASE}/recipients/${id}`, 'DELETE', null, 'Recipient deleted');
        loadView('recipients');
    }
}


// --- Items ---
async function renderItems() {
    const res = await fetch(`${API_BASE}/items`);
    const data = await res.json();

    let html = `
        <div class="table-container">
            <div class="table-header">
                <h3>All Donated Items</h3>
                <button class="btn btn-primary" onclick="openItemForm()"><i class="fa-solid fa-plus"></i> Log Item</button>
            </div>
            <table>
                <thead>
                    <tr>
                        <th>Description</th>
                        <th>Category</th>
                        <th>Condition</th>
                        <th>Donor</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${data.map(d => `
                        <tr>
                            <td><strong>${d.description}</strong></td>
                            <td style="text-transform:capitalize;">${d.category}</td>
                            <td><span class="badge badge-${d.itemCondition.toLowerCase()}">${d.itemCondition}</span></td>
                            <td>${d.donor ? d.donor.name : 'Unknown'}</td>
                            <td>
                                ${d.distributed 
                                    ? `<span class="badge badge-success" title="To: ${d.recipient?.organizationName}">Distributed</span>` 
                                    : '<span class="badge badge-warning">In Stock</span>'}
                            </td>
                            <td class="actions-cell">
                                <button class="btn btn-sm btn-edit" onclick="openItemForm(${d.id})"><i class="fa-solid fa-pen"></i></button>
                                ${!d.distributed ? `<button class="btn btn-sm btn-success" onclick="openDistributeForm(${d.id})"><i class="fa-solid fa-truck"></i> Distribute</button>` : ''}
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
    contentArea.innerHTML = html;
}

async function openItemForm(id = null) {
    const isEdit = id !== null;
    // Fetch dropdown data
    const drives = await (await fetch(`${API_BASE}/drives`)).json();
    const donors = await (await fetch(`${API_BASE}/donors`)).json();

    modalTitle.textContent = isEdit ? 'Edit Donated Item' : 'Log Donated Item';
    modalBody.innerHTML = `
        <form onsubmit="submitItem(event, ${id})">
            <div class="form-group">
                <label>Description (e.g. Blue Jacket)</label>
                <input type="text" class="form-control" id="i_desc" required>
            </div>
            <div style="display:flex; gap:16px;">
                <div class="form-group" style="flex:1;">
                    <label>Category</label>
                    <select class="form-control" id="i_cat">
                        <option value="clothes">Clothes</option>
                        <option value="books">Books</option>
                        <option value="other">Other</option>
                    </select>
                </div>
                <div class="form-group" style="flex:1;">
                    <label>Condition</label>
                    <select class="form-control" id="i_cond">
                        <option value="NEW">New</option>
                        <option value="GOOD">Good</option>
                        <option value="WORN">Worn</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label>Belongs to Drive</label>
                <select class="form-control" id="i_drive" required>
                    ${drives.map(d => `<option value="${d.id}">${d.name}</option>`).join('')}
                </select>
            </div>
            <div class="form-group">
                <label>Donor</label>
                <select class="form-control" id="i_donor" required>
                    ${donors.map(d => `<option value="${d.id}">${d.name}</option>`).join('')}
                </select>
            </div>
            <div class="form-actions">
                <button type="button" class="btn btn-edit" onclick="closeModal()">Cancel</button>
                <button type="submit" class="btn btn-primary">${isEdit ? 'Update' : 'Save'} Item</button>
            </div>
        </form>
    `;
    
    if (isEdit) {
        fetch(`${API_BASE}/items/${id}`).then(res => res.json()).then(data => {
            document.getElementById('i_desc').value = data.description;
            document.getElementById('i_cat').value = data.category.toLowerCase();
            document.getElementById('i_cond').value = data.itemCondition.toUpperCase();
            if(data.drive) document.getElementById('i_drive').value = data.drive.id;
            if(data.donor) document.getElementById('i_donor').value = data.donor.id;
            openModal();
        });
    } else {
        openModal();
    }
}

async function submitItem(e, id) {
    e.preventDefault();
    const payload = {
        description: document.getElementById('i_desc').value,
        category: document.getElementById('i_cat').value,
        itemCondition: document.getElementById('i_cond').value,
        driveId: parseInt(document.getElementById('i_drive').value),
        donorId: parseInt(document.getElementById('i_donor').value)
    };
    
    if (id) {
        await apiCall(`${API_BASE}/items/${id}`, 'PUT', payload, 'Item updated successfully');
    } else {
        await apiCall(`${API_BASE}/items`, 'POST', payload, 'Item logged successfully');
    }
    
    closeModal();
    loadView('items');
}

async function openDistributeForm(itemId) {
    const recipients = await (await fetch(`${API_BASE}/recipients`)).json();
    
    modalTitle.textContent = 'Distribute Item';
    modalBody.innerHTML = `
        <form onsubmit="submitDistribution(event, ${itemId})">
            <p style="margin-bottom:20px; color:var(--text-muted);">Select a recipient organization to distribute this item to.</p>
            <div class="form-group">
                <label>Recipient Organization</label>
                <select class="form-control" id="dist_recip" required>
                    ${recipients.map(r => `<option value="${r.id}">${r.organizationName}</option>`).join('')}
                </select>
            </div>
            <div class="form-actions">
                <button type="button" class="btn btn-edit" onclick="closeModal()">Cancel</button>
                <button type="submit" class="btn btn-success"><i class="fa-solid fa-truck"></i> Confirm Distribution</button>
            </div>
        </form>
    `;
    openModal();
}

async function submitDistribution(e, itemId) {
    e.preventDefault();
    const payload = {
        itemId: itemId,
        recipientId: parseInt(document.getElementById('dist_recip').value)
    };
    await apiCall(`${API_BASE}/items/distribute`, 'PUT', payload, 'Item distributed successfully');
    closeModal();
    loadView('items');
}


// --- Utilities ---
async function apiCall(url, method, body, successMsg) {
    try {
        const options = { method };
        if (body) {
            options.headers = { 'Content-Type': 'application/json' };
            options.body = JSON.stringify(body);
        }
        
        const res = await fetch(url, options);
        if (!res.ok) {
            let errMsg = 'Operation failed';
            try {
                const data = await res.json();
                errMsg = data.message || (data.messages ? Object.values(data.messages)[0] : errMsg);
            } catch(e) {}
            throw new Error(errMsg);
        }
        
        showToast(successMsg);
        return true;
    } catch (err) {
        showToast(err.message, true);
        throw err;
    }
}

function openModal() {
    modalOverlay.classList.remove('hidden');
}

function closeModal() {
    modalOverlay.classList.add('hidden');
}

function showToast(message, isError = false) {
    toastMessage.textContent = message;
    if (isError) {
        toast.classList.add('error');
        toast.querySelector('i').className = 'fa-solid fa-triangle-exclamation';
    } else {
        toast.classList.remove('error');
        toast.querySelector('i').className = 'fa-solid fa-circle-check';
    }
    
    toast.classList.remove('hidden');
    setTimeout(() => {
        toast.classList.add('hidden');
    }, 3000);
}
