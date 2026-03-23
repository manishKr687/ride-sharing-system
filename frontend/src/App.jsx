import { useState } from "react";

const initialBaseUrls = {
  ride: "http://localhost:7071",
  user: "http://localhost:7072",
  driver: "http://localhost:7073",
  payment: "http://localhost:7075"
};

const initialWorkflow = {
  userId: null,
  userMeta: "Create a rider to populate ride and payment forms.",
  driverId: null,
  driverMeta: "Create a driver to populate assignment forms.",
  rideId: null,
  rideMeta: "Request a ride to unlock assignment, completion, and payment.",
  paymentId: null,
  paymentMeta: "Process payment manually after a completed ride.",
  statusTone: "idle"
};

const initialForms = {
  user: {
    name: "",
    email: "",
    role: "RIDER",
    currentLatitude: "",
    currentLongitude: ""
  },
  driver: {
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

const sections = [
  {
    key: "user",
    title: "Register Rider",
    description: "Creates a user in user-service.",
    button: "Register Rider",
    successLabel: "Rider created",
    resetOnSuccess: ["name", "email", "currentLatitude", "currentLongitude"],
    fields: [
      { name: "name", label: "Name", type: "text", placeholder: "Aarav Sharma" },
      { name: "email", label: "Email", type: "email", placeholder: "aarav@example.com" },
      { name: "role", label: "Role", type: "select", options: ["RIDER", "DRIVER"] },
      { name: "currentLatitude", label: "Latitude", type: "number", placeholder: "28.6139", step: "any", optional: true },
      { name: "currentLongitude", label: "Longitude", type: "number", placeholder: "77.2090", step: "any", optional: true }
    ]
  },
  {
    key: "driver",
    title: "Register Driver",
    description: "Creates a driver in driver-service.",
    button: "Register Driver",
    successLabel: "Driver created",
    resetOnSuccess: ["name", "phoneNumber", "vehicleNumber", "currentLatitude", "currentLongitude"],
    fields: [
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
    description: "This is still manual in your backend. It triggers payment-service directly.",
    button: "Process Payment",
    helperLabel: "Use Latest Ride + Rider",
    successLabel: "Payment processed",
    fields: [
      { name: "rideId", label: "Ride ID", type: "number", placeholder: "1", min: "1" },
      { name: "userId", label: "User ID", type: "number", placeholder: "1", min: "1" },
      { name: "amount", label: "Amount", type: "number", placeholder: "250.00", min: "0", step: "0.01" }
    ]
  }
];

function App() {
  const [baseUrls, setBaseUrls] = useState(initialBaseUrls);
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
    appendLog("Request", { url, ...options });
    const response = await fetch(url, options);
    const text = await response.text();
    let body = text;

    try {
      body = text ? JSON.parse(text) : {};
    } catch {
      body = text;
    }

    if (!response.ok) {
      appendLog("Response Error", { status: response.status, body });
      throw new Error(`Request failed with status ${response.status}`);
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

      if (key === "user") {
        response = await request(`${baseUrls.user}/api/v1/users/register`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
        applyWorkflowDefaults({
          userId: response.userId,
          userMeta: `${response.name} � ${response.email}`,
          statusTone: "success"
        });
      }

      if (key === "driver") {
        response = await request(`${baseUrls.driver}/api/v2/drivers/register`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
        applyWorkflowDefaults({
          driverId: response.driverId,
          driverMeta: `${response.name} � ${response.vehicleNumber}`,
          statusTone: "success"
        });
      }

      if (key === "rideRequest") {
        response = await request(`${baseUrls.ride}/api/v1/ride/request`, {
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
        response = await request(`${baseUrls.ride}/api/v1/ride/assign/${payload.rideId}?driverId=${payload.driverId}`, {
          method: "POST"
        });
        applyWorkflowDefaults({
          rideId: response.rideId,
          driverId: response.driverId,
          rideMeta: `Assigned driver #${response.driverId} � status ${response.status}`,
          driverMeta: `Assigned to ride #${response.rideId}`,
          statusTone: "success"
        });
      }

      if (key === "completeRide") {
        response = await request(`${baseUrls.ride}/api/v1/ride/complete/${payload.rideId}`, {
          method: "POST"
        });
        applyWorkflowDefaults({
          rideId: response.rideId,
          rideMeta: `Completed � ${response.pickupLocation} -> ${response.dropLocation}`,
          statusTone: "success"
        });
      }

      if (key === "payment") {
        response = await request(`${baseUrls.payment}/api/v1/payment`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(payload)
        });
        applyWorkflowDefaults({
          paymentId: response.paymentId,
          paymentMeta: `${response.status} � ${response.message}`,
          statusTone: response.status === "SUCCESS" ? "success" : "warning"
        });
      }

      resetFields(key, section.resetOnSuccess);
      appendLog("Workflow", section.successLabel);
    } catch (error) {
      applyWorkflowDefaults({ statusTone: "error" });
      appendLog("Frontend Error", error.message);
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
          <h1>React dashboard for your current Uber-style backend.</h1>
          <p className="hero-text">
            This frontend mirrors the workflow your backend supports today: register users and drivers,
            request a ride, assign a driver, complete the ride, and then process payment manually.
          </p>
        </div>
        <div className="hero-card">
          <p className="hero-card-label">Workflow</p>
          <ol>
            <li>Create rider</li>
            <li>Create driver</li>
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
            <h2>Service URLs</h2>
            <p>Change these if you are not using the Docker Compose host ports.</p>
          </div>
          <div className="form-grid two-col">
            <Field label="Ride Service"><input value={baseUrls.ride} onChange={(e) => setBaseUrls((c) => ({ ...c, ride: e.target.value }))} /></Field>
            <Field label="User Service"><input value={baseUrls.user} onChange={(e) => setBaseUrls((c) => ({ ...c, user: e.target.value }))} /></Field>
            <Field label="Driver Service"><input value={baseUrls.driver} onChange={(e) => setBaseUrls((c) => ({ ...c, driver: e.target.value }))} /></Field>
            <Field label="Payment Service"><input value={baseUrls.payment} onChange={(e) => setBaseUrls((c) => ({ ...c, payment: e.target.value }))} /></Field>
          </div>
        </section>

        <section className={`panel panel-workflow status-${workflow.statusTone}`}>
          <div className="panel-heading">
            <h2>Live Workflow State</h2>
            <p>The app tracks the latest records so the next step can be filled automatically.</p>
          </div>
          <div className="workflow-grid">
            <StateCard label="Latest Rider" value={workflow.userId ? `User #${workflow.userId}` : "Not created yet"} meta={workflow.userMeta} />
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
