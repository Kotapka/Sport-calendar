const API_BASE = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', () => {
    showSection('events');
    initEventListeners();
});

// --- TOAST NOTIFICATIONS ---
function showToast(message, type = 'error') {
    const container = document.getElementById('toast-container');
    const toast = document.createElement('div');
    const bgColor = type === 'error' ? 'bg-red-600' : 'bg-emerald-600';

    toast.className = `${bgColor} text-white px-6 py-4 rounded-2xl shadow-2xl flex items-center gap-3 animate-bounce-in min-w-[300px] transition-all duration-500 mb-3`;
    toast.innerHTML = `
        <i class="fa-solid ${type === 'error' ? 'fa-circle-exclamation' : 'fa-circle-check'} text-xl"></i>
        <div class="flex-1 font-bold uppercase tracking-tight text-xs">${message}</div>
    `;

    container.appendChild(toast);
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 500);
    }, 4000);
}

// --- NAVIGATION ---
function showSection(section) {
    const eventsSec = document.getElementById('events-section');
    const teamsSec = document.getElementById('teams-section');
    const navActions = document.getElementById('nav-actions');

    if (section === 'events') {
        eventsSec.classList.remove('hidden');
        teamsSec.classList.add('hidden');
        navActions.innerHTML = `<button onclick="openCreateEventModal()" class="bg-emerald-500 text-zinc-950 px-6 py-2.5 rounded-full font-bold text-xs uppercase tracking-widest hover:scale-105 transition-all"><i class="fa-solid fa-plus mr-2"></i> New Event</button>`;
        loadEvents();
    } else {
        eventsSec.classList.add('hidden');
        teamsSec.classList.remove('hidden');
        navActions.innerHTML = `<button onclick="openCreateTeamModal()" class="bg-blue-500 text-zinc-950 px-6 py-2.5 rounded-full font-bold text-xs uppercase tracking-widest hover:scale-105 transition-all"><i class="fa-solid fa-shield mr-2"></i> New Club</button>`;
        loadTeams();
    }
}

// --- TEAMS DATA ---
async function loadTeams() {
    const grid = document.getElementById('teams-grid');
    grid.innerHTML = '<div class="col-span-full text-center py-10 opacity-50 uppercase tracking-widest text-xs font-bold italic">Fetching clubs...</div>';

    try {
        const res = await fetch(`${API_BASE}/teams`);
        const teams = await res.json();
        grid.innerHTML = '';

        teams.forEach(t => {
            const card = document.createElement('div');
            card.className = 'bg-zinc-900 border border-zinc-800 rounded-3xl p-6 text-center hover:border-blue-500/50 transition-all group relative overflow-hidden';
            card.innerHTML = `
                <div class="w-16 h-16 bg-zinc-800 rounded-2xl flex items-center justify-center mx-auto mb-4 text-2xl group-hover:scale-110 transition-all">🛡️</div>
                <div class="font-black uppercase tracking-tight mb-1 text-sm">${t.name}</div>
                <div class="text-[10px] text-zinc-500 font-bold uppercase mb-4">${t.teamCountryCode || 'INT'}</div>
                <div class="flex gap-4 justify-center pt-4 border-t border-zinc-800/50 opacity-0 group-hover:opacity-100 transition-all">
                    <button onclick='openEditTeamModal(${JSON.stringify(t).replace(/'/g, "&apos;")})' class="text-[10px] font-black text-blue-400 uppercase hover:underline">Edit</button>
                    <button onclick="deleteTeam(${t.teamId})" class="text-[10px] font-black text-red-500 uppercase hover:underline">Delete</button>
                </div>
            `;
            grid.appendChild(card);
        });
    } catch (e) {
        showToast("Could not load clubs list.");
    }
}

async function handleTeamSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('teamId').value;
    const data = {
        name: document.getElementById('tName').value,
        officialName: document.getElementById('tOfficialName').value,
        slug: document.getElementById('tSlug').value,
        abbreviation: document.getElementById('tAbbr').value,
        teamCountryCode: document.getElementById('tCountry').value
    };

    try {
        const res = await fetch(id ? `${API_BASE}/teams/${id}` : `${API_BASE}/teams`, {
            method: id ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });

        if (res.ok) {
            showToast(id ? "Club data updated!" : "New club added!", 'success');
            closeModals();
            loadTeams();
        } else showToast("Failed to save club data.");
    } catch (e) { showToast("Server connection error."); }
}

function openCreateTeamModal() {
    document.getElementById('team-form').reset();
    document.getElementById('teamId').value = '';
    document.getElementById('team-modal-title').innerText = "New Club Profile";
    document.getElementById('team-modal').classList.remove('hidden');
}

function openEditTeamModal(team) {
    document.getElementById('teamId').value = team.teamId;
    document.getElementById('tName').value = team.name;
    document.getElementById('tOfficialName').value = team.officialName || '';
    document.getElementById('tSlug').value = team.slug || '';
    document.getElementById('tAbbr').value = team.abbreviation || '';
    document.getElementById('tCountry').value = team.teamCountryCode || '';
    document.getElementById('team-modal-title').innerText = "Edit Club Profile";
    document.getElementById('team-modal').classList.remove('hidden');
}

async function deleteTeam(id) {
    if (!confirm("Are you sure? This cannot be undone.")) return;
    try {
        const res = await fetch(`${API_BASE}/teams/${id}`, { method: 'DELETE' });
        if (res.ok) { showToast("Club removed from database.", 'success'); loadTeams(); }
        else showToast("Deletion failed. Check if club has active matches.");
    } catch (e) { showToast("Connection error."); }
}

// --- EVENTS DATA ---
async function loadEvents() {
    const grid = document.getElementById('events-grid');
    const sortType = document.getElementById('event-sort')?.value || 'newest';
    grid.innerHTML = '<div class="col-span-full text-center py-10 opacity-50 uppercase tracking-widest text-xs font-bold italic">Fetching events...</div>';

    try {
        const res = await fetch(`${API_BASE}/events`);
        let events = await res.json();

        // CLIENT-SIDE SORTING
        events.sort((a, b) => {
            const dateA = new Date(`${a.dateVenue}T${a.timeVenueUtc}`);
            const dateB = new Date(`${b.dateVenue}T${b.timeVenueUtc}`);
            if (sortType === 'newest') return dateB - dateA;
            if (sortType === 'oldest') return dateA - dateB;
            if (sortType === 'status') return a.status.localeCompare(b.status);
            return 0;
        });

        grid.innerHTML = '';
        if (events.length === 0) {
            grid.innerHTML = '<div class="col-span-full text-center py-10 text-zinc-600 uppercase text-xs font-black">No matches found in schedule</div>';
            return;
        }

        events.forEach(ev => {
            const isFin = ev.status === 'finished';
            const card = document.createElement('div');
            card.className = 'bg-zinc-900 border border-zinc-800 rounded-[2rem] p-8 transition-all hover:border-emerald-500/50 group relative overflow-hidden';
            card.innerHTML = `
                <div class="flex justify-between items-center mb-6">
                    <span class="text-[9px] font-black uppercase tracking-widest px-3 py-1 rounded-full ${isFin ? 'bg-zinc-800 text-zinc-500' : 'bg-emerald-500/10 text-emerald-500'}">
                        ${ev.status}
                    </span>
                    <span class="text-[10px] font-mono text-zinc-600 font-bold">${ev.dateVenue} • ${ev.timeVenueUtc.substring(0, 5)}</span>
                </div>
                <div class="flex justify-between items-center gap-2 mb-6">
                    <div class="flex-1 text-center font-bold uppercase text-xs truncate text-zinc-300">${ev.homeTeamName}</div>
                    <div class="text-2xl font-black text-emerald-500 bg-black/40 px-4 py-2 rounded-2xl shadow-inner">${ev.homeGoals ?? '-'}:${ev.awayGoals ?? '-'}</div>
                    <div class="flex-1 text-center font-bold uppercase text-xs truncate text-zinc-300">${ev.awayTeamName}</div>
                </div>
                <div class="flex justify-between pt-4 border-t border-zinc-800/50 opacity-0 group-hover:opacity-100 transition-all">
                    <button onclick="openEditEventModal(${ev.eventId})" class="text-[10px] font-black uppercase text-zinc-500 hover:text-emerald-400 transition-colors">Edit Match</button>
                    <button onclick="deleteEvent(${ev.eventId})" class="text-[10px] font-black uppercase text-zinc-500 hover:text-red-500 transition-colors">Remove</button>
                </div>`;
            grid.appendChild(card);
        });
    } catch (e) { showToast("Could not sync with events database."); }
}

async function handleEventSubmit(e) {
    e.preventDefault();
    const id = document.getElementById('eventId').value;
    const status = document.getElementById('status').value;
    const payload = {
        season: 2026,
        status: status,
        dateVenue: document.getElementById('dateVenue').value,
        timeVenueUtc: document.getElementById('timeVenueUtc').value + (document.getElementById('timeVenueUtc').value.length === 5 ? ":00" : ""),
        homeTeamId: parseInt(document.getElementById('homeTeamSelect').value),
        awayTeamId: parseInt(document.getElementById('awayTeamSelect').value),
        stageId: 1,
        homeGoals: status === 'scheduled' ? null : parseInt(document.getElementById('homeGoals').value || 0),
        awayGoals: status === 'scheduled' ? null : parseInt(document.getElementById('awayGoals').value || 0)
    };

    if (!validateEventData(payload)) return;

    try {
        const res = await fetch(id ? `${API_BASE}/events/${id}` : `${API_BASE}/events`, {
            method: id ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            showToast(id ? "Match updated!" : "Event scheduled!", 'success');
            closeModals();
            loadEvents();
        } else showToast("Error syncing event with server.");
    } catch (e) { showToast("Connection lost."); }
}

// --- VALIDATION & HELPERS ---
function validateEventData(data) {
    const now = new Date();
    const eventTime = new Date(`${data.dateVenue}T${data.timeVenueUtc}`);
    if (data.homeTeamId === data.awayTeamId) { showToast("Teams must be different!"); return false; }
    if (data.status === 'live' && eventTime > now) { showToast("Cannot set LIVE status before kickoff!"); return false; }
    if (data.status === 'finished' && eventTime > now) { showToast("Cannot finish a future event!"); return false; }
    return true;
}

function initEventListeners() {
    document.getElementById('event-form').addEventListener('submit', handleEventSubmit);
    document.getElementById('team-form').addEventListener('submit', handleTeamSubmit);
    document.getElementById('status').addEventListener('change', (e) => toggleGoalsVisibility(e.target.value));
    document.getElementById('tName').addEventListener('input', (e) => {
        document.getElementById('tSlug').value = e.target.value.toLowerCase().replace(/\s+/g, '-').replace(/[^\w-]+/g, '');
    });
}

function toggleGoalsVisibility(status) {
    const container = document.getElementById('goals-container');
    status === 'scheduled' ? container.classList.add('hidden') : container.classList.remove('hidden');
}

async function populateTeamSelects() {
    const res = await fetch(`${API_BASE}/teams`);
    const teams = await res.json();
    const options = teams.map(t => `<option value="${t.teamId}">${t.name}</option>`).join('');
    document.getElementById('homeTeamSelect').innerHTML = document.getElementById('awayTeamSelect').innerHTML = options;
}

function openCreateEventModal() {
    document.getElementById('event-form').reset();
    document.getElementById('eventId').value = '';
    document.getElementById('event-modal-title').innerText = "Schedule New Match";
    toggleGoalsVisibility('scheduled');
    populateTeamSelects();
    document.getElementById('event-modal').classList.remove('hidden');
}

async function openEditEventModal(id) {
    const res = await fetch(`${API_BASE}/events/${id}`);
    const ev = await res.json();
    await populateTeamSelects();
    document.getElementById('eventId').value = ev.eventId;
    document.getElementById('dateVenue').value = ev.dateVenue;
    document.getElementById('timeVenueUtc').value = ev.timeVenueUtc.substring(0, 5);
    document.getElementById('status').value = ev.status;
    document.getElementById('homeTeamSelect').value = ev.homeTeamId;
    document.getElementById('awayTeamSelect').value = ev.awayTeamId;
    toggleGoalsVisibility(ev.status);
    if (ev.status !== 'scheduled') {
        document.getElementById('homeGoals').value = ev.homeGoals ?? 0;
        document.getElementById('awayGoals').value = ev.awayGoals ?? 0;
    }
    document.getElementById('event-modal-title').innerText = "Update Match Data";
    document.getElementById('event-modal').classList.remove('hidden');
}

async function deleteEvent(id) {
    if (confirm("Remove event from schedule?")) {
        const res = await fetch(`${API_BASE}/events/${id}`, { method: 'DELETE' });
        if (res.ok) { showToast("Event deleted.", 'success'); loadEvents(); }
    }
}

function closeModals() {
    document.getElementById('event-modal').classList.add('hidden');
    document.getElementById('team-modal').classList.add('hidden');
}