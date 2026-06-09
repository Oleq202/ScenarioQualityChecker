function addNode(container, type) {
    const nodeDiv = document.createElement('div');
    nodeDiv.className = type === 'STEP' ? 'step-box' : 'subscenario-box';
    nodeDiv.dataset.type = type;

    if (type === 'STEP') {
        nodeDiv.innerHTML = `
            <span>Step: </span>
            <input type="text" class="desc-input" placeholder="Actor action...">
            <button class="delete-btn" onclick="this.parentElement.remove()">X</button>
        `;
    } else if (type === 'SUBSCENARIO') {
        nodeDiv.innerHTML = `
            <span>Subscenario: </span>
            <select class="type-input">
                <option value="IF">IF</option>
                <option value="ELSE">ELSE</option>
                <option value="FOR_EACH">FOR_EACH</option>
            </select>
            <input type="text" class="desc-input" placeholder="Condition...">
            <button class="delete-btn" onclick="this.parentElement.remove()">X</button>
            <div class="children-container"></div>
            <div class="controls">
                <button onclick="addNode(this.parentElement.previousElementSibling, 'STEP')">+ Step</button>
                <button onclick="addNode(this.parentElement.previousElementSibling, 'SUBSCENARIO')">+ Subscenario</button>
            </div>
        `;
    }
    container.appendChild(nodeDiv);
}

function extractSteps(container) {
    const steps = [];
    Array.from(container.children).forEach(child => {
        if (!child.dataset.type) return;

        const description = child.querySelector('.desc-input').value;

        if (child.dataset.type === 'STEP') {
            steps.push({ type: "STEP", description: description });
        } else if (child.dataset.type === 'SUBSCENARIO') {
            const scenarioType = child.querySelector('.type-input').value;
            const childrenContainer = child.querySelector('.children-container');

            steps.push({
                type: "SUBSCENARIO",
                scenario_type: scenarioType,
                description: description,
                steps: extractSteps(childrenContainer)
            });
        }
    });
    return steps;
}

async function analyze(endpoint) {
    const outputArea = document.getElementById('outputArea');
    outputArea.textContent = "Loading...";

    const payload = {
        title: document.getElementById('title').value,
        systemActor: document.getElementById('systemActor').value,
        actors: document.getElementById('actors').value.split(',').map(s => s.trim()).filter(s => s.length > 0),
        steps: extractSteps(document.getElementById('editor-root'))
    };

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = endpoint.includes('numbered') ? await response.text() : await response.json();
        outputArea.textContent = typeof result === 'string' ? result : JSON.stringify(result, null, 2);

    } catch (error) {
        outputArea.textContent = "Error: " + error.message;
        console.error(error);
    }
}

function loadScenario() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a .json file first.");
        return;
    }

    const reader = new FileReader();
    reader.onload = function(event) {
        try {
            const scenarioData = JSON.parse(event.target.result);
            populateUI(scenarioData);
        } catch (error) {
            alert("Error parsing JSON file. Please ensure it is formatted correctly.");
            console.error(error);
        }
    };
    reader.readAsText(file);
}

function downloadScenario() {
    const titleRaw = document.getElementById('title').value;
    const payload = {
        title: titleRaw,
        systemActor: document.getElementById('systemActor').value,
        actors: document.getElementById('actors').value.split(',').map(s => s.trim()).filter(s => s.length > 0),
        steps: extractSteps(document.getElementById('editor-root'))
    };

    const jsonString = JSON.stringify(payload, null, 2);

    const blob = new Blob([jsonString], { type: "application/json" });

    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;

    const safeTitle = (titleRaw || "scenario").replace(/[^a-z0-9]/gi, '_').toLowerCase();
    a.download = `${safeTitle}.json`;

    document.body.appendChild(a);
    a.click();

    document.body.removeChild(a);
    URL.revokeObjectURL(url);
}

function populateUI(data) {
    document.getElementById('title').value = data.title || "";
    document.getElementById('systemActor').value = data.systemActor || "";

    if (data.actors && Array.isArray(data.actors)) {
        document.getElementById('actors').value = data.actors.join(", ");
    } else {
        document.getElementById('actors').value = "";
    }

    const root = document.getElementById('editor-root');
    root.innerHTML = '';

    if (data.steps && Array.isArray(data.steps)) {
        data.steps.forEach(step => {
            renderNode(root, step);
        });
    }
}

function renderNode(container, nodeData) {
    const nodeDiv = document.createElement('div');
    nodeDiv.className = nodeData.type === 'STEP' ? 'step-box' : 'subscenario-box';
    nodeDiv.dataset.type = nodeData.type;

    const safeDesc = nodeData.description ? nodeData.description.replace(/"/g, '&quot;') : '';

    if (nodeData.type === 'STEP') {
        nodeDiv.innerHTML = `
            <span>Step: </span>
            <input type="text" class="desc-input" value="${safeDesc}">
            <button class="delete-btn" onclick="this.parentElement.remove()">X</button>
        `;
    } else if (nodeData.type === 'SUBSCENARIO') {

        const isIf = nodeData.scenario_type === 'IF' ? 'selected' : '';
        const isElse = nodeData.scenario_type === 'ELSE' ? 'selected' : '';
        const isForEach = nodeData.scenario_type === 'FOR_EACH' ? 'selected' : '';

        nodeDiv.innerHTML = `
            <span>Subscenario: </span>
            <select class="type-input">
                <option value="IF" ${isIf}>IF</option>
                <option value="ELSE" ${isElse}>ELSE</option>
                <option value="FOR_EACH" ${isForEach}>FOR_EACH</option>
            </select>
            <input type="text" class="desc-input" value="${safeDesc}">
            <button class="delete-btn" onclick="this.parentElement.remove()">X</button>
            <div class="children-container"></div>
            <div class="controls">
                <button onclick="addNode(this.parentElement.previousElementSibling, 'STEP')">+ Step</button>
                <button onclick="addNode(this.parentElement.previousElementSibling, 'SUBSCENARIO')">+ Subscenario</button>
            </div>
        `;

        const childrenContainer = nodeDiv.querySelector('.children-container');

        if (nodeData.steps && Array.isArray(nodeData.steps)) {
            nodeData.steps.forEach(childNode => {
                renderNode(childrenContainer, childNode);
            });
        }
    }

    container.appendChild(nodeDiv);
}

window.onload = () => {
    addNode(document.getElementById('editor-root'), 'STEP');
};