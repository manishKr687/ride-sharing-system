const consoleOutput = document.getElementById("consoleOutput");
const clearConsoleButton = document.getElementById("clearConsole");

const forms = {
    userForm: document.getElementById("userForm"),
    driverForm: document.getElementById("driverForm"),
    rideRequestForm: document.getElementById("rideRequestForm"),
    assignDriverForm: document.getElementById("assignDriverForm"),
    completeRideForm: document.getElementById("completeRideForm"),
    paymentForm: document.getElementById("paymentForm")
};

function getBaseUrls() {
    return {
        ride: document.getElementById("rideBaseUrl").value.trim(),
        user: document.getElementById("userBaseUrl").value.trim(),
        driver: document.getElementById("driverBaseUrl").value.trim(),
        payment: document.getElementById("paymentBaseUrl").value.trim()
    };
}

function appendLog(title, payload) {
    const time = new Date().toLocaleTimeString();
    const formatted = typeof payload === "string" ? payload : JSON.stringify(payload, null, 2);
    consoleOutput.textContent = `[${time}] ${title}\n${formatted}\n\n${consoleOutput.textContent}`;
}

function formToJson(form) {
    const data = new FormData(form);
    return Object.fromEntries(
        [...data.entries()].map(([key, value]) => [key, normalizeValue(value)])
    );
}

function normalizeValue(value) {
    if (value === "") {
        return null;
    }

    const numericPattern = /^-?\d+(\.\d+)?$/;
    if (numericPattern.test(value)) {
        return Number(value);
    }

    return value;
}

async function request(url, options = {}) {
    appendLog("Request", { url, ...options });

    const response = await fetch(url, options);
    const text = await response.text();

    let body = text;
    try {
        body = text ? JSON.parse(text) : {};
    } catch (error) {
        body = text;
    }

    if (!response.ok) {
        appendLog("Response Error", { status: response.status, body });
        throw new Error(`Request failed with status ${response.status}`);
    }

    appendLog("Response", { status: response.status, body });
    return body;
}

async function handleSubmit(form, callback) {
    form.addEventListener("submit", async (event) => {
        event.preventDefault();
        const submitButton = form.querySelector("button[type='submit']");
        submitButton.disabled = true;
        submitButton.textContent = "Working...";

        try {
            await callback();
        } catch (error) {
            appendLog("Frontend Error", error.message);
        } finally {
            submitButton.disabled = false;
            submitButton.textContent = submitButton.dataset.originalText || submitButton.textContent.replace("Working...", "Submit");
        }
    });
}

for (const form of Object.values(forms)) {
    const button = form.querySelector("button[type='submit']");
    button.dataset.originalText = button.textContent;
}

handleSubmit(forms.userForm, async () => {
    const urls = getBaseUrls();
    const payload = formToJson(forms.userForm);

    await request(`${urls.user}/api/v1/users/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });
});

handleSubmit(forms.driverForm, async () => {
    const urls = getBaseUrls();
    const payload = formToJson(forms.driverForm);

    await request(`${urls.driver}/api/v2/drivers/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });
});

handleSubmit(forms.rideRequestForm, async () => {
    const urls = getBaseUrls();
    const payload = formToJson(forms.rideRequestForm);

    await request(`${urls.ride}/api/v1/ride/request`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });
});

handleSubmit(forms.assignDriverForm, async () => {
    const urls = getBaseUrls();
    const payload = formToJson(forms.assignDriverForm);

    await request(`${urls.ride}/api/v1/ride/assign/${payload.rideId}?driverId=${payload.driverId}`, {
        method: "POST"
    });
});

handleSubmit(forms.completeRideForm, async () => {
    const urls = getBaseUrls();
    const payload = formToJson(forms.completeRideForm);

    await request(`${urls.ride}/api/v1/ride/complete/${payload.rideId}`, {
        method: "POST"
    });
});

handleSubmit(forms.paymentForm, async () => {
    const urls = getBaseUrls();
    const payload = formToJson(forms.paymentForm);

    await request(`${urls.payment}/api/v1/payment`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });
});

clearConsoleButton.addEventListener("click", () => {
    consoleOutput.textContent = "Console cleared.";
});
