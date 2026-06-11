const state = {
  users: [],
  plans: [],
  tiers: [],
  selectedUserId: null,
  selectedPlanType: null,
  membership: null,
  eligibleTier: null,
  activity: [],
};

const refs = {
  usersList: document.getElementById("usersList"),
  tierLadder: document.getElementById("tierLadder"),
  plansGrid: document.getElementById("plansGrid"),
  benefitsGrid: document.getElementById("benefitsGrid"),
  activityLog: document.getElementById("activityLog"),
  toastContainer: document.getElementById("toastContainer"),
  userSelect: document.getElementById("userSelect"),
  planSelect: document.getElementById("planSelect"),
  tierSelect: document.getElementById("tierSelect"),
  subscribeForm: document.getElementById("subscribeForm"),
  orderForm: document.getElementById("orderForm"),
  orderAmount: document.getElementById("orderAmount"),
  refreshBtn: document.getElementById("refreshBtn"),
  checkEligibleBtn: document.getElementById("checkEligibleBtn"),
  upgradeBtn: document.getElementById("upgradeBtn"),
  downgradeBtn: document.getElementById("downgradeBtn"),
  cancelBtn: document.getElementById("cancelBtn"),
  heroTitle: document.getElementById("heroTitle"),
  heroSubtitle: document.getElementById("heroSubtitle"),
  heroStatus: document.getElementById("heroStatus"),
  heroEligibleTier: document.getElementById("heroEligibleTier"),
  heroProgress: document.getElementById("heroProgress"),
  heroProgressLabel: document.getElementById("heroProgressLabel"),
  heroPlan: document.getElementById("heroPlan"),
  heroTier: document.getElementById("heroTier"),
  heroExpiry: document.getElementById("heroExpiry"),
};

async function api(path, options = {}) {
  const response = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  const contentType = response.headers.get("content-type") || "";
  const body = contentType.includes("application/json") ? await response.json() : await response.text();

  if (!response.ok) {
    const message = body && typeof body === "object" && body.error ? body.error : response.statusText;
    throw new Error(message);
  }

  return body;
}

function toast(message, type = "info") {
  const node = document.createElement("div");
  node.className = `toast ${type}`;
  node.textContent = message;
  refs.toastContainer.appendChild(node);
  setTimeout(() => node.remove(), 3000);
}

function logActivity(title, payload) {
  state.activity.unshift({
    time: new Date().toLocaleTimeString(),
    title,
    payload,
  });
  state.activity = state.activity.slice(0, 10);
  renderActivity();
}

function findUser() {
  return state.users.find((user) => user.id === state.selectedUserId) || null;
}

function findPlan(planType) {
  return state.plans.find((plan) => plan.planType === planType) || null;
}

function findTier(tierName) {
  return state.tiers.find((tier) => tier.tierName === tierName) || null;
}

function selectedTierBenefits() {
  if (state.membership?.tierName) {
    return findTier(state.membership.tierName)?.benefits || [];
  }
  if (state.eligibleTier) {
    return findTier(state.eligibleTier)?.benefits || [];
  }
  return [];
}

function renderUsers() {
  refs.usersList.innerHTML = "";
  refs.userSelect.innerHTML = "";

  state.users.forEach((user) => {
    const card = document.createElement("button");
    card.type = "button";
    card.className = `user-card ${user.id === state.selectedUserId ? "active" : ""}`;
    card.innerHTML = `
      <strong>${user.name}</strong>
      <div class="muted">${user.email}</div>
      <div class="pill">${user.cohort}</div>
    `;
    card.addEventListener("click", () => {
      state.selectedUserId = user.id;
      refs.userSelect.value = String(user.id);
      renderUsers();
      loadUserContext();
    });
    refs.usersList.appendChild(card);

    const option = document.createElement("option");
    option.value = String(user.id);
    option.textContent = `${user.name} (${user.cohort})`;
    refs.userSelect.appendChild(option);
  });

  if (state.selectedUserId) {
    refs.userSelect.value = String(state.selectedUserId);
  }
}

function renderPlans() {
  refs.plansGrid.innerHTML = "";
  refs.planSelect.innerHTML = "";

  state.plans.forEach((plan) => {
    const card = document.createElement("button");
    card.type = "button";
    card.className = `plan-card ${plan.planType === state.selectedPlanType ? "active" : ""}`;
    card.innerHTML = `
      <div class="pill">${plan.planType}</div>
      <h3>${plan.label}</h3>
      <p class="muted">Duration: ${plan.durationDays} days</p>
      <strong>${Number(plan.price).toFixed(2)}</strong>
    `;
    card.addEventListener("click", () => {
      state.selectedPlanType = plan.planType;
      refs.planSelect.value = plan.planType;
      renderPlans();
    });
    refs.plansGrid.appendChild(card);

    const option = document.createElement("option");
    option.value = plan.planType;
    option.textContent = `${plan.planType} - ${plan.label}`;
    refs.planSelect.appendChild(option);
  });

  if (state.selectedPlanType) {
    refs.planSelect.value = state.selectedPlanType;
  } else if (state.plans.length) {
    state.selectedPlanType = state.plans[0].planType;
    refs.planSelect.value = state.selectedPlanType;
  }
}

function renderTierLadder() {
  refs.tierLadder.innerHTML = "";
  refs.tierSelect.innerHTML = "";

  state.tiers.forEach((tier) => {
    const card = document.createElement("div");
    card.className = `tier-card tier-${tier.tierName.toLowerCase()}`;
    card.innerHTML = `
      <div class="pill">${tier.tierName}</div>
      <h3>${tier.tierLevel === 1 ? "Silver" : tier.tierLevel === 2 ? "Gold" : "Platinum"}</h3>
      <p class="muted">${tier.benefits.length} benefits available</p>
    `;
    refs.tierLadder.appendChild(card);

    const option = document.createElement("option");
    option.value = tier.tierName;
    option.textContent = tier.tierName;
    refs.tierSelect.appendChild(option);
  });
}

function renderMembershipHero() {
  const user = findUser();
  const membership = state.membership;
  const eligibleTier = state.eligibleTier || "-";
  const activeTierBenefits = selectedTierBenefits();
  const now = new Date();
  const start = membership ? new Date(`${membership.startDate}T00:00:00`) : null;
  const end = membership ? new Date(`${membership.endDate}T00:00:00`) : null;
  const totalDays = start && end ? Math.max(1, (end - start) / 86400000) : 0;
  const elapsedDays = start ? Math.max(0, (now - start) / 86400000) : 0;
  const progress = membership ? Math.max(8, Math.min(100, (elapsedDays / totalDays) * 100)) : 14;
  const daysLeft = end ? Math.max(0, Math.ceil((end - now) / 86400000)) : 0;

  refs.heroTitle.textContent = user ? `${user.name}'s membership` : "Select a user to get started";
  refs.heroSubtitle.textContent = membership
    ? `${membership.planType} plan running from ${membership.startDate} to ${membership.endDate}. ${daysLeft} day(s) remaining.`
    : user
      ? "This user currently has no active membership."
      : "Membership status, expiry, and benefits will appear here.";
  refs.heroStatus.textContent = membership ? membership.status : "No active membership";
  refs.heroEligibleTier.textContent = `Eligible tier: ${eligibleTier}`;
  refs.heroPlan.textContent = membership ? membership.planLabel : "-";
  refs.heroTier.textContent = membership ? membership.tierName : eligibleTier;
  refs.heroExpiry.textContent = membership ? membership.endDate : "-";

  refs.heroProgress.style.width = `${progress}%`;
  refs.heroProgressLabel.textContent = membership
    ? `${daysLeft} day(s) left. Showing ${membership.benefits.length} benefits for the active tier.`
    : activeTierBenefits.length
      ? `Showing ${activeTierBenefits.length} benefits for the suggested tier.`
      : "No benefits available yet.";
}

function renderBenefits() {
  refs.benefitsGrid.innerHTML = "";
  const benefits = selectedTierBenefits();

  if (!benefits.length) {
    refs.benefitsGrid.innerHTML = '<div class="benefit-card muted">No benefits to display yet.</div>';
    return;
  }

  benefits.forEach((benefit) => {
    const card = document.createElement("div");
    card.className = "benefit-card";
    card.innerHTML = `
      <div class="pill">${benefit.benefitType}</div>
      <h3>${benefit.description}</h3>
      <p class="muted">Value: ${benefit.value}</p>
    `;
    refs.benefitsGrid.appendChild(card);
  });
}

function renderActivity() {
  refs.activityLog.innerHTML = "";
  if (!state.activity.length) {
    refs.activityLog.innerHTML = '<div class="log-entry"><strong>No activity yet</strong><span class="muted">Use the forms above to start interacting.</span></div>';
    return;
  }

  state.activity.forEach((entry) => {
    const row = document.createElement("div");
    row.className = "log-entry";
    row.innerHTML = `<strong>${entry.title}</strong><span class="muted">${entry.time}</span><div class="muted">${typeof entry.payload === "string" ? entry.payload : JSON.stringify(entry.payload)}</div>`;
    refs.activityLog.appendChild(row);
  });
}

function renderAll() {
  renderUsers();
  renderPlans();
  renderTierLadder();
  renderMembershipHero();
  renderBenefits();
  renderActivity();
}

async function loadCatalog() {
  try {
    const [users, plans, tiers] = await Promise.all([
      api("/api/users"),
      api("/api/plans"),
      api("/api/tiers"),
    ]);
    state.users = users;
    state.plans = plans;
    state.tiers = tiers;
    if (!state.selectedUserId && users.length) {
      state.selectedUserId = users[0].id;
    }
    if (!state.selectedPlanType && plans.length) {
      state.selectedPlanType = plans[0].planType;
    }
    renderAll();
    await loadUserContext();
  } catch (error) {
    toast(error.message, "error");
    logActivity("Failed to load catalog", error.message);
  }
}

async function loadUserContext() {
  const user = findUser();
  if (!user) {
    renderAll();
    return;
  }

  try {
    state.membership = await api(`/api/membership/${user.id}`);
  } catch {
    state.membership = null;
  }

  try {
    const eligible = await api(`/api/membership/${user.id}/eligible-tier`);
    state.eligibleTier = eligible.eligibleTier;
  } catch (error) {
    state.eligibleTier = null;
    toast(error.message, "error");
  }

  renderAll();
  logActivity("Loaded user context", { user: user.name, eligibleTier: state.eligibleTier || "-" });
}

async function handleSubscribe(event) {
  event.preventDefault();
  try {
    const body = {
      userId: Number(refs.userSelect.value),
      planType: refs.planSelect.value,
      tierName: refs.tierSelect.value,
    };
    const result = await api("/api/membership/subscribe", {
      method: "POST",
      body: JSON.stringify(body),
    });
    state.selectedUserId = result.userId;
    state.membership = result;
    state.eligibleTier = result.tierName;
    toast("Subscription created successfully", "success");
    logActivity("Subscribed user", result);
    renderAll();
  } catch (error) {
    toast(error.message, "error");
    logActivity("Subscription failed", error.message);
  }
}

async function handleMembershipAction(action) {
  if (!state.selectedUserId) {
    toast("Select a user first", "error");
    return;
  }
  try {
    const response = await api(`/api/membership/${state.selectedUserId}/${action}`, { method: "PUT" });
    state.membership = response;
    toast(`Membership ${action}d`, "success");
    logActivity(`Membership ${action}`, response);
    await loadUserContext();
  } catch (error) {
    toast(error.message, "error");
    logActivity(`Membership ${action} failed`, error.message);
  }
}

async function handleCancel() {
  if (!state.selectedUserId) {
    toast("Select a user first", "error");
    return;
  }
  try {
    await api(`/api/membership/${state.selectedUserId}`, { method: "DELETE" });
    state.membership = null;
    toast("Membership cancelled", "success");
    logActivity("Membership cancelled", `User ${state.selectedUserId}`);
    await loadUserContext();
  } catch (error) {
    toast(error.message, "error");
    logActivity("Cancel failed", error.message);
  }
}

async function handlePlaceOrder(event) {
  event.preventDefault();
  if (!state.selectedUserId) {
    toast("Select a user first", "error");
    return;
  }
  try {
    const result = await api("/api/orders", {
      method: "POST",
      body: JSON.stringify({
        userId: state.selectedUserId,
        amount: Number(refs.orderAmount.value),
      }),
    });
    toast("Order placed successfully", "success");
    logActivity("Placed order", result);
    refs.orderAmount.value = "";
    await loadUserContext();
  } catch (error) {
    toast(error.message, "error");
    logActivity("Order failed", error.message);
  }
}

async function checkEligibleTier() {
  if (!state.selectedUserId) {
    toast("Select a user first", "error");
    return;
  }
  try {
    const response = await api(`/api/membership/${state.selectedUserId}/eligible-tier`);
    state.eligibleTier = response.eligibleTier;
    renderAll();
    toast(`Eligible tier: ${response.eligibleTier}`, "success");
    logActivity("Checked eligible tier", response);
  } catch (error) {
    toast(error.message, "error");
    logActivity("Eligibility check failed", error.message);
  }
}

refs.subscribeForm.addEventListener("submit", handleSubscribe);
refs.orderForm.addEventListener("submit", handlePlaceOrder);
refs.refreshBtn.addEventListener("click", loadCatalog);
refs.checkEligibleBtn.addEventListener("click", checkEligibleTier);
refs.upgradeBtn.addEventListener("click", () => handleMembershipAction("upgrade"));
refs.downgradeBtn.addEventListener("click", () => handleMembershipAction("downgrade"));
refs.cancelBtn.addEventListener("click", handleCancel);

refs.userSelect.addEventListener("change", () => {
  state.selectedUserId = Number(refs.userSelect.value);
  loadUserContext();
});

refs.planSelect.addEventListener("change", () => {
  state.selectedPlanType = refs.planSelect.value;
  renderPlans();
});

refs.tierSelect.addEventListener("change", () => {
  renderMembershipHero();
  renderBenefits();
});

loadCatalog();
