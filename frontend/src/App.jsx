import { useState } from "react";

const DEFAULT_GATEWAY = "http://localhost:8080";

const initialForms = {
  login: {
    email: "",
    password: ""
  },
  user: {
    name: "",
    email: "",
    password: "",
    role: "RIDER",
    currentLatitude: "",
    currentLongitude: ""
  },
  driver: {
    userId: "",
    name: "",
    phoneNumber: "",
    vehicleNumber: "",
    driverStatus: "AVAILABLE",
    currentLatitude: "",
    currentLongitude: ""
  },
  rideRequest: {
    userId: "",
    pickupLocation: "",
    dropLocation: ""
  },
  assignDriver: {
    rideId: "",
    driverId: ""
  },
  completeRide: {
    rideId: ""
  },
  payment: {
    rideId: "",
    userId: "",
    amount: ""
  }
};

const initialWorkflow = {
  userId: null,
  userMeta: "Register or log in to populate ride and payment forms.",
  driverId: null,
  driverMeta: "Create a driver to populate assignment forms.",
  rideId: null,
  rideMeta: "Request a ride to unlock assignment, completion, and payment.",
  paymentId: null,
  paymentMeta: "Process payment manually after a completed ride.",
  statusTone: "idle"
};

const sections = [
  {
    key: "login",
    title: "Login",
    description: "Authenticate and receive a JWT token for all subsequent requests.",
    button: "Login",
    successLabel: "Logged in",
    resetOnSuccess: [],
    fields: [
      { name: "email", label: "Email", type: "email", placeholder: "aarav@example.com" },
      { name: "password", label: "Password", type: "password", placeholder: "••••••••" }
    ]
  },
  {
    key: "user",
    title: "Register User",
    description: "Creates a user in user-service and returns a JWT token.",
    button: "Register User",
    successLabel: "User created",
    resetOnSuccess: ["name", "email", "password", "currentLatitude", "currentLongitude"],
    fields: [
      { name: "name", label: "Name", type: "text", placeholder: "Aarav Sharma" },
      { name: "email", label: "Email", type: "email", placeholder: "aarav@example.com" },
      { name: "password", label: "Password", type: "password", placeholder: "••••••••" },
      { name: "role", label: "Role", type: "select", options: ["RIDER", "DRIVER"] },
      { name: "currentLatitude", label: "Latitude", type: "number", placeholder: "28.6139", step: "any", optional: true },
      { name: "currentLongitude", label: "Longitude", type: "number", placeholder: "77.2090", step: "any", optional: true }
    ]
  },
  {
    key: "driver",
    title: "Register Driver Profile",
    description: "Links a driver profile to an existing user. User ID auto-fills after registration.",
    button: "Register Driver",
    successLabel: "Driver created",
    resetOnSuccess: ["name", "phoneNumber", "vehicleNumber", "currentLatitude", "currentLongitude"],
    fields: [
      { name: "userId", label: "User ID", type: "number", placeholder: "1", min: "1" },
      { name: "name", label: "Name", type: "text", placeholder: "Riya Patel" },
      { name: "phoneNumber", label: "Phone Number", type: "text", placeholder: "+91-9999999999" },
      { name: "vehicleNumber", label: "Vehicle Number", type: "text", placeholder: "DL01AB1234" },
      { name: "driverStatus", label: "Status", type: "select", options: ["AVAILABLE", "OFFLINE", "ASSIGNED"] },
      { name: "currentLatitude", label: "Latitude", type: "number", placeholder: "28.7041", step: "any", optional: true },
      { name: "currentLongitude", label: "Longitude", type: "number", placeholder: "77.1025", step: "any", optional: true }
    ]
  },
  {
    key: "rideRequest",
    title: "Request Ride",
    description: "Creates a ride in ride-service and emits a REQUESTED event.",
    button: "Request Ride",
    successLabel: "Ride requested",
    resetOnSuccess: ["pickupLocation", "dropLocation"],
    fields: [
      { name: "userId", label: "User ID", type: "number", placeholder: "1", min: "1" },
      { name: "pickupLocation", label: "Pickup", type: "text", placeholder: "Connaught Place, Delhi" },
      { name: "dropLocation", label: "Drop", type: "text", placeholder: "Noida Sector 18" }
    ]
  },
  {
    key: "assignDriver",
    title: "Assign Driver",
    description: "Marks the driver as assigned, updates ride status, and emits an ASSIGNED event.",
    button: "Assign Driver",
    helperLabel: "Use Latest Ride + Driver",
    successLabel: "Driver assigned",
    fields: [
      { name: "rideId", label: "Ride ID", type: "number", placeholder: "1", min: "1" },
      { name: "driverId", label: "Driver ID", type: "number", placeholder: "1", min: "1" }
    ]
  },
  {
    key: "completeRide",
    title: "Complete Ride",
    description: "Marks the ride as completed and emits a COMPLETED event.",
    button: "Complete Ride",
    helperLabel: "Use Latest Ride",
    successLabel: "Ride completed",
    fields: [
      { name: "rideId", label: "Ride ID", type: "number", placeholder: "1", min: "1" }
    ]
  },
  {
    key: "payment",
    title: "Process Payment",
    description: "Triggers payment-service directly via the gateway.",
    button: "Process Payment",
    helperLabel: "Use Latest Ride + Rider",
    successLabel: "Payment processed",
    fields: [
      { name: "rideId", label: "Ride ID", type: "number", placeholder: "1", min: "1" },
      { name: "userId", label: "User ID", type: "number", placeholder: "1", min: "1" },
      { name: "amount", label: "Amount", type: "number", placeholder: "250.00", min: "0.01", step: "0.01" }
    ]
  }
];

function App() {
  const [gatewayUrl, setGatewayUrl] = useState(DEFAULT_GATEWAY);
  const [token, setToken] = useState(null);
  const [forms, setForms] = useState(initialForms);
  const [workflow, setWorkflow] = useState(initialWorkflow);
  const [logs, setLogs] = useState(["Frontend ready."]);
  const [loading, setLoading] = useState({});

  function appendLog(title, payload) {
    const time = new Date().toLocaleTimeString();
    const rendered = typeof payload === "string" ? payload : JSON.stringify(payload, null, 2);
    setLogs((current) => [`[${time}] ${title}\n${rendered}`, ...current]);
  }

  function normalizeValue(value) {
    if (value === "") return null;
    if (/^-?\d+(\.\d+)?$/.test(value)) return Number(value);
    return value;
  }

  function buildPayload(key) {
    return Object.fromEntries(
      Object.entries(forms[key]).map(([field, value]) => [field, normalizeValue(value)])
    );
  }

  function setFormField(group, field, value) {
    setForms((current) => ({
      ...current,
      [group]: {
        ...current[group],
        [field]: value
      }
    }));
  }

  function resetFields(group, fields) {
    if (!fields || fields.length === 0) return;
    setForms((current) => ({
      ...current,
      [group]: Object.fromEntries(
        Object.entries(current[group]).map(([key, value]) => [
          key,
          fields.includes(key) ? "" : value
        ])
      )
    }));
  }

  function applyWorkflowDefaults(patch) {
    setWorkflow((current) => ({ ...current, ...patch }));
    setForms((current) => ({
      ...current,
      driver: {
        ...current.driver,
        userId: patch.userId ?? current.driver.userId
      },
      rideRequest: {
        ...current.rideRequest,
        userId: patch.userId ?? current.rideRequest.userId
      },
      assignDriver: {
        ...current.assignDriver,
        rideId: patch.rideId ?? current.assignDriver.rideId,
        driverId: patch.driverId ?? current.assignDriver.driverId
      },
      completeRide: {
        ...current.completeRide,
        rideId: patch.rideId ?? current.completeRide.rideId
      },
      payment: {
        ...current.payment,
        rideId: patch.rideId ?? current.payment.rideId,
        userId: patch.userId ?? current.payment.userId
      }
    }));
  }

  async function request(url, options = {}) {
    appendLog("Request", { url, method: options.method || "GET" });
    const headers = { ...(options.headers || {}) };
    if (token) headers["Authorization"] = `Bearer ${token}`;
    const response = await fetch(url, { ...options, headers });
    const text = await response.text();
    let body = text;
    try {
      body = text ? JSON.parse(text) : {};
    } catch {
      body = text;
    }
    if (!response.ok) {
      appendLog("Response Error", { status: response.status, body });
      const serverMessage =
        (body && typeof body === "object" && (body.message || body.error)) ||
        (typeof body === "string" && body) ||
        `HTTP ${response.status}`;
      const err = new Error(serverMessage);
      err.body = body;
      throw err;
    }
    appendLog("Response", { status: response.status, body });
    return body;
  }

  async function handleSubmit(section) {
    const key = section.key;
    setLoading((current) => ({ ...current, [key]: true }));
    try {
      const payload = buildPayload(key);
      let response;

      if (key === "login") {
        response = await request(`${gatewayUrl}/api/v1/users/login`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
        setToken(response.token);
        applyWorkflowDefaults({
          userId: response.userId,
          userMeta: `${response.name} (${response.role}) — logged in`,
          statusTone: "success"
        });
      }

      if (key === "user") {
        response = await request(`${gatewayUrl}/api/v1/users/register`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
        setToken(response.token);
        applyWorkflowDefaults({
          userId: response.userId,
          userMeta: `${response.name} → ${response.email} (${response.role})`,
          statusTone: "success"
        });
      }

      if (key === "driver") {
        response = await request(`${gatewayUrl}/api/v2/drivers/register`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
        applyWorkflowDefaults({
          driverId: response.driverId,
          driverMeta: `${response.name} → ${response.vehicleNumber}`,
          statusTone: "success"
        });
      }

      if (key === "rideRequest") {
        response = await request(`${gatewayUrl}/api/v1/ride/request`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
        applyWorkflowDefaults({
          rideId: response.rideId,
          rideMeta: `${response.pickupLocation} -> ${response.dropLocation}`,
          statusTone: "success"
        });
      }

      if (key === "assignDriver") {
        response = await request(`${gatewayUrl}/api/v1/ride/assign/${payload.rideId}?driverId=${payload.driverId}`, {
          method: "POST"
        });
        applyWorkflowDefaults({
          rideId: response.rideId,
          driverId: response.driverId,
          rideMeta: `Assigned driver #${response.driverId} → status ${response.status}`,
          driverMeta: `Assigned to ride #${response.rideId}`,
          statusTone: "success"
        });
      }

      if (key === "completeRide") {
        response = await request(`${gatewayUrl}/api/v1/ride/complete/${payload.rideId}`, {
          method: "POST"
        });
        applyWorkflowDefaults({
          rideId: response.rideId,
          rideMeta: `Completed → ${response.pickupLocation} -> ${response.dropLocation}`,
          statusTone: "success"
        });
      }

      if (key === "payment") {
        response = await request(`${gatewayUrl}/api/v1/payment`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
        applyWorkflowDefaults({
          paymentId: response.paymentId,
          paymentMeta: `${response.status} → ${response.message}`,
          statusTone: response.status === "SUCCESS" ? "success" : "warning"
        });
      }

      resetFields(key, section.resetOnSuccess);
      appendLog("Workflow", section.successLabel);
    } catch (error) {
      applyWorkflowDefaults({ statusTone: "error" });
      const detail = error.body ? JSON.stringify(error.body, null, 2) : error.message;
      appendLog("Frontend Error", detail);
    } finally {
      setLoading((current) => ({ ...current, [key]: false }));
    }
  }

  function useLatestAssignment() {
    setForms((current) => ({
      ...current,
      assignDriver: {
        ...current.assignDriver,
        rideId: workflow.rideId ?? current.assignDriver.rideId,
        driverId: workflow.driverId ?? current.assignDriver.driverId
      }
    }));
    appendLog("Helper", "Filled assignment form with the latest ride and driver IDs.");
  }

  function useLatestRide() {
    setForms((current) => ({
      ...current,
      completeRide: {
        ...current.completeRide,
        rideId: workflow.rideId ?? current.completeRide.rideId
      }
    }));
    appendLog("Helper", "Filled completion form with the latest ride ID.");
  }

  function useLatestPaymentInputs() {
    setForms((current) => ({
      ...current,
      payment: {
        ...current.payment,
        rideId: workflow.rideId ?? current.payment.rideId,
        userId: workflow.userId ?? current.payment.userId
      }
    }));
    appendLog("Helper", "Filled payment form with the latest ride and rider IDs.");
  }

  return (
    <div className="page-shell">
      <header className="hero">
        <div className="hero-copy">
          <p className="eyebrow">Ride Sharing System</p>
          <h1>React dashboard for your Uber-style backend.</h1>
          <p className="hero-text">
            All requests route through the API Gateway. Register or log in first to obtain a JWT token,
            which is sent automatically with every subsequent request.
          </p>
        </div>
        <div className="hero-card">
          <p className="hero-card-label">Workflow</p>
          <ol>
            <li>Register or log in</li>
            <li>Register driver profile</li>
            <li>Request ride</li>
            <li>Assign driver</li>
            <li>Complete ride</li>
            <li>Process payment</li>
          </ol>
        </div>
      </header>

      <main className="dashboard-grid">
        <section className="panel panel-config">
          <div className="panel-heading">
            <h2>Gateway URL</h2>
            <p>Single entry point for all services. Change only if not using Docker Compose defaults.</p>
          </div>
          <div className="form-grid">
            <Field label="API Gateway">
              <input value={gatewayUrl} onChange={(e) => setGatewayUrl(e.target.value)} />
            </Field>
            {token && (
              <Field label="JWT Token">
                <input readOnly value={token} style={{ fontFamily: "monospace", fontSize: "11px" }} />
              </Field>
            )}
          </div>
        </section>

        <section className={`panel panel-workflow status-${workflow.statusTone}`}>
          <div className="panel-heading">
            <h2>Live Workflow State</h2>
            <p>The app tracks the latest records so the next step can be filled automatically.</p>
          </div>
          <div className="workflow-grid">
            <StateCard label="Current User" value={workflow.userId ? `User #${workflow.userId}` : "Not authenticated"} meta={workflow.userMeta} />
            <StateCard label="Latest Driver" value={workflow.driverId ? `Driver #${workflow.driverId}` : "Not created yet"} meta={workflow.driverMeta} />
            <StateCard label="Latest Ride" value={workflow.rideId ? `Ride #${workflow.rideId}` : "Not created yet"} meta={workflow.rideMeta} />
            <StateCard label="Latest Payment" value={workflow.paymentId ? `Payment #${workflow.paymentId}` : "Not processed yet"} meta={workflow.paymentMeta} />
          </div>
        </section>

        {sections.map((section) => (
          <section className="panel" key={section.key}>
            <div className="panel-heading">
              <h2>{section.title}</h2>
              <p>{section.description}</p>
            </div>

            {section.key === "assignDriver" && (
              <div className="helper-row">
                <button type="button" className="secondary-button" onClick={useLatestAssignment}>{section.helperLabel}</button>
              </div>
            )}

            {section.key === "completeRide" && (
              <div className="helper-row">
                <button type="button" className="secondary-button" onClick={useLatestRide}>{section.helperLabel}</button>
              </div>
            )}

            {section.key === "payment" && (
              <div className="helper-row">
                <button type="button" className="secondary-button" onClick={useLatestPaymentInputs}>{section.helperLabel}</button>
              </div>
            )}

            <form className={`form-grid ${section.fields.length === 2 ? "two-col" : ""}`} onSubmit={(event) => { event.preventDefault(); handleSubmit(section); }}>
              {section.fields.map((field) => (
                <Field key={field.name} label={field.label}>
                  {field.type === "select" ? (
                    <select value={forms[section.key][field.name]} onChange={(e) => setFormField(section.key, field.name, e.target.value)}>
                      {field.options.map((option) => <option key={option} value={option}>{option}</option>)}
                    </select>
                  ) : (
                    <input
                      type={field.type}
                      placeholder={field.placeholder}
                      value={forms[section.key][field.name]}
                      onChange={(e) => setFormField(section.key, field.name, e.target.value)}
                      min={field.min}
                      step={field.step}
                      required={!field.optional}
                    />
                  )}
                </Field>
              ))}
              <button type="submit" disabled={loading[section.key]}>{loading[section.key] ? "Working..." : section.button}</button>
            </form>
          </section>
        ))}

        <section className="panel panel-log">
          <div className="panel-heading">
            <h2>Response Console</h2>
            <p>Quick visibility into request payloads, API responses, and errors.</p>
          </div>
          <div className="console-actions">
            <button type="button" className="secondary-button" onClick={() => setLogs(["Console cleared."])}>Clear Console</button>
          </div>
          <pre className="console-output">{logs.join("\n\n")}</pre>
        </section>
      </main>
    </div>
  );
}

function Field({ label, children }) {
  return (
    <label>
      <span>{label}</span>
      {children}
    </label>
  );
}

function StateCard({ label, value, meta }) {
  return (
    <article className="state-card">
      <p className="state-label">{label}</p>
      <strong>{value}</strong>
      <span className="state-meta">{meta}</span>
    </article>
  );
}

export default App;
