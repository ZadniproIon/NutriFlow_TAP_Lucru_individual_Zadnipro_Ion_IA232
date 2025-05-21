document.addEventListener("DOMContentLoaded", () => {
  showSection("main");
  updateMainPage();
});


// ==== Default Settings & Persistence ====
const SETTINGS_KEY = "nutriflow_settings";
let settings = JSON.parse(localStorage.getItem(SETTINGS_KEY)) || {
  theme: "light",
  calorieGoal: 2200,
  stepsGoal: 10000
};

// Aplică tema la pornire
function applyTheme() {
  document.body.classList.toggle("dark-mode", settings.theme === "dark");
  // actualizează butonul (opțional)
  document.getElementById("btn-toggle-theme").innerText =
    settings.theme === "dark" ? "Light Mode" : "Dark Mode";
}
applyTheme();

// Încarcă valorile inițiale în câmpuri
document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("input-calorie-goal").value = settings.calorieGoal;
  document.getElementById("input-steps-goal").value = settings.stepsGoal;
});




const sections = {
    main: document.getElementById("main-window-content"),
    diary: document.getElementById("diary-window-content"),
    analytics: document.getElementById("analytics-window-content"),
    fooddatabase: document.getElementById("fooddatabase-window-content"),
    exerciseandsteps: document.getElementById("exerciseandsteps-window-content"),
    settings: document.getElementById("settings-window-content"),
};

const buttons = {
    main: document.getElementById("btn-main"),
    diary: document.getElementById("btn-diary"),
    analytics: document.getElementById("btn-analytics"),
    fooddatabase: document.getElementById("btn-fooddatabase"),
    exerciseandsteps: document.getElementById("btn-exercise"),
    settings: document.getElementById("btn-settings"),
    // Adaugă aici și celelalte când le creezi
};

function showSection(name) {
    // Ascunde toate secțiunile
    Object.values(sections).forEach(section => section.style.display = "none");

    // Scoate clasa "active" de pe toate butoanele
    Object.values(buttons).forEach(btn => btn.classList.remove("active"));

    // Afișează secțiunea aleasă și marchează butonul activ
    sections[name].style.display = "flex";
    buttons[name].classList.add("active");
}

// Setează evenimentele pe butoane
buttons.main.addEventListener("click", () => {
  showSection("main");
  updateMainPage();
});

buttons.diary.addEventListener("click", () => showSection("diary"));
buttons.analytics.addEventListener("click", () => showSection("analytics"));
buttons.fooddatabase.addEventListener("click", () => showSection("fooddatabase"));
buttons.exerciseandsteps.addEventListener("click", () => showSection("exerciseandsteps"));
buttons.settings.addEventListener("click", () => showSection("settings"));

// Adaugă și celelalte

// Pornire implicită pe Main
showSection("main");









// ==== datele tale, direct în JS ====
const foods = [
    { id:"1",  name:"Oats",                  calories:389, protein:16.9, carbs:66.3, fats:6.9,  weight:100 },
    { id:"2",  name:"Whole Milk",            calories:61,  protein:3.2,  carbs:4.8,  fats:3.3,  weight:100 },
    { id:"3",  name:"Chicken Breast (cooked)",calories:165, protein:31,   carbs:0,    fats:3.6,  weight:100 },
    { id:"4",  name:"Brown Rice (cooked)",   calories:111, protein:2.6,  carbs:23,   fats:0.9,  weight:100 },
    { id:"5",  name:"Boiled Egg",            calories:155, protein:13,   carbs:1.1,  fats:11,   weight:100 },
    { id:"6",  name:"Apple (raw)",           calories:52,  protein:0.3,  carbs:14,   fats:0.2,  weight:100 },
    { id:"7",  name:"Banana",                calories:89,  protein:1.1,  carbs:23,   fats:0.3,  weight:100 },
    { id:"8",  name:"Greek Yogurt (plain)",  calories:59,  protein:10,   carbs:3.6,  fats:0.4,  weight:100 },
    { id:"9",  name:"Peanut Butter",         calories:588, protein:25,   carbs:20,   fats:50,   weight:100 },
    { id:"10", name:"Broccoli (steamed)",    calories:35,  protein:2.4,  carbs:7.2,  fats:0.4,  weight:100 }
];
  
  const meals = [
    { id:"m1",  date:"2025-05-21", mealType:"breakfast", foodId:"1",  grams:80  },
    { id:"m2",  date:"2025-05-21", mealType:"breakfast", foodId:"2",  grams:200 },
    { id:"m3",  date:"2025-05-21", mealType:"lunch",     foodId:"3",  grams:150 },
    { id:"m4",  date:"2025-05-21", mealType:"lunch",     foodId:"4",  grams:200 },
    { id:"m5",  date:"2025-05-21", mealType:"lunch",     foodId:"10", grams:150 },
    { id:"m6",  date:"2025-05-21", mealType:"snack",     foodId:"6",  grams:150 },
    { id:"m7",  date:"2025-05-21", mealType:"snack",     foodId:"9",  grams:30  },
    { id:"m8",  date:"2025-05-21", mealType:"dinner",    foodId:"5",  grams:120 },
    { id:"m9",  date:"2025-05-21", mealType:"dinner",    foodId:"7",  grams:100 },
    { id:"m10", date:"2025-05-21", mealType:"dinner",    foodId:"8",  grams:150 }
  ];

  
  
  function loadDiaryForToday() {
    const today = "2025-05-21";
    const mealsContainer = document.getElementById("meals-container");
    mealsContainer.innerHTML = "";
  
    // grupăm mesele pentru azi
    const grouped = meals
      .filter(m => m.date === today)
      .reduce((acc, m) => {
        (acc[m.mealType] = acc[m.mealType] || []).push(m);
        return acc;
      }, {});
  
    const mealOrder = ["breakfast","lunch","dinner","snack"];
    let totalCalories = 0;
  
    mealOrder.forEach(type => {
      const items = grouped[type];
      if (!items?.length) return;
  
      const section = document.createElement("div");
      section.className = "diary-card-meal";
  
      const title = document.createElement("div");
      title.className = "diary-card-meal-title-and-cal";
      title.innerHTML = `
        <p>${type[0].toUpperCase()+type.slice(1)}</p>
        <p class="meal-total">0 cal</p>
      `;
      section.appendChild(title);
      section.appendChild(document.createElement("hr"));
  
      let mealCal = 0;
      items.forEach(item => {
        const food = foods.find(f => f.id === item.foodId);
        const factor = item.grams / food.weight;
        const cal    = food.calories * factor;
        const prot   = food.protein  * factor;
        const carb   = food.carbs    * factor;
        const fat    = food.fats     * factor;
  
        mealCal += cal;
        totalCalories += cal;
  
        // creez intrarea
        const entry = document.createElement("div");
        entry.className = "diary-card-food-item";
        entry.innerHTML = `
          <p>
            ${food.name} (${item.grams}g) - ${Math.round(cal)} cal<br>
            Protein: ${Math.round(prot)}g, Carbs: ${Math.round(carb)}g, Fats: ${Math.round(fat)}g
          </p>
          <i class="fa-solid fa-square-xmark delete-icon"></i>
        `;
  
        // ataşez evenimentul de ştergere
        const del = entry.querySelector(".delete-icon");
        del.addEventListener("click", () => {
          // confirm dacă vrei
          if (!confirm(`Remove ${food.name} from ${type}?`)) return;
  
          // găsesc index şi şterg din array
          const idx = meals.findIndex(m => m.id === item.id);
          if (idx > -1) meals.splice(idx, 1);
  
          // reîncarc Diary
          loadDiaryForToday();
        });
  
        section.appendChild(entry);
      });
  
      // actualizez totalul mesei
      title.querySelector(".meal-total").innerText = `${Math.round(mealCal)} cal`;
      mealsContainer.appendChild(section);
    });
  
    // actualizez formula de sus
    const [goalEl, foodEl, exEl, remEl] =
      document.querySelectorAll("#diary-card-formula strong");
    const goal = settings.calorieGoal;
    goalEl.innerText = goal;
    foodEl.innerText = Math.round(totalCalories);
    exEl.innerText   = 0;
    remEl.innerText  = goal - Math.round(totalCalories);
  }
  
  

  
  // 5) inițial
  document.addEventListener("DOMContentLoaded", ()=>{
    showSection("main");
    loadDiaryForToday();
    loadFoodDatabase();
    // dacă vrei Diary la start, folosește:
    // showSection("diary"); loadDiaryForToday();
  });




  function loadFoodDatabase() {
    const container = document.getElementById("food-db-container");
    container.innerHTML = "";
  
    foods.forEach(food => {
      // card principal
      const card = document.createElement("div");
      card.classList.add("food-db-item");
  
      // partea stângă: date produs
      const info = document.createElement("div");
      info.innerHTML = `
        <p><strong>${food.name} (${food.weight}g)</strong> - ${food.calories} cal</p>
        <p style="font-size: 14px;">
          Protein ${food.protein}g, Carbs ${food.carbs}g, Fats ${food.fats}g
        </p>
      `;
  
      // partea dreaptă: butoane
      const actions = document.createElement("div");
      actions.style.display = "flex";
      actions.style.gap = "12px";
      actions.innerHTML = `
        <i class="fa-solid fa-pen-to-square" style="cursor: pointer;"></i>
        <i class="fa-solid fa-trash" style="cursor: pointer;"></i>
      `;
  
      // listener pe ștergere
      const deleteIcon = actions.querySelector(".fa-trash");
      deleteIcon.addEventListener("click", () => {
        // confirmare opțională
        if (!confirm(`Ștergi „${food.name}”?`)) return;
        // găsești indexul și elimini din array
        const idx = foods.findIndex(f => f.id === food.id);
        if (idx > -1) foods.splice(idx, 1);
        // reîncarci lista
        loadFoodDatabase();
      });
  
      card.appendChild(info);
      card.appendChild(actions);
      container.appendChild(card);
    });
  }
  

  document.getElementById("btn-add-food").addEventListener("click", () => {
    const name   = document.getElementById("new-food-name").value.trim();
    const prot   = parseFloat(document.getElementById("new-food-protein").value);
    const carb   = parseFloat(document.getElementById("new-food-carbs").value);
    const fat    = parseFloat(document.getElementById("new-food-fats").value);
    const weight = parseFloat(document.getElementById("new-food-weight").value);
  
    if (!name || isNaN(prot) || isNaN(carb) || isNaN(fat) || isNaN(weight)) {
      alert("Please fill all fields correctly.");
      return;
    }
  
    // Calculezi caloriile per 100g proporțional:
    const calories = Math.round(((prot * 4) + (carb * 4) + (fat * 9)) / weight * 100);
  
    // Creezi un ID unic simplu:
    const newId = String(foods.length + 1);
  
    foods.push({ id:newId, name, calories, protein:prot, carbs:carb, fats:fat, weight });
  
    loadFoodDatabase();
  
    // Golești formularul
    ["new-food-name","new-food-protein","new-food-carbs","new-food-fats","new-food-weight"]
      .forEach(id => document.getElementById(id).value = "");
  });
  


// --- Elemente form Diary ---
const mealSelect   = document.getElementById("meal-select");
const foodSelect   = document.getElementById("food-select");
const gramsInput   = document.getElementById("grams-input");
const btnAddMeal   = document.getElementById("btn-add-meal");

// populate lista de mâncăruri la load
function populateFoodSelect() {
  foodSelect.innerHTML = '<option value="">Select food</option>';
  foods.forEach(f => {
    const opt = document.createElement("option");
    opt.value = f.id;
    opt.textContent = f.name;
    foodSelect.appendChild(opt);
  });
}

// actualizează câmpurile de macros pe măsură ce user-ul selectează și completează
function updateDiaryMacros() {
  const foodId = foodSelect.value;
  const grams  = parseFloat(gramsInput.value);
  if (!foodId || isNaN(grams) || grams <= 0) {
    // reset la zero
    document.getElementById("macro-protein").textContent = "0g";
    document.getElementById("macro-carbs").textContent   = "0g";
    document.getElementById("macro-fats").textContent    = "0g";
    document.getElementById("macro-calories").textContent= "0 cal";
    return;
  }
  const food = foods.find(f => f.id === foodId);
  const factor = grams / food.weight;
  const prot  = Math.round(food.protein * factor);
  const carb  = Math.round(food.carbs   * factor);
  const fat   = Math.round(food.fats    * factor);
  const cal   = Math.round(food.calories * factor);

  document.getElementById("macro-protein").textContent  = prot + "g";
  document.getElementById("macro-carbs").textContent    = carb + "g";
  document.getElementById("macro-fats").textContent     = fat  + "g";
  document.getElementById("macro-calories").textContent = cal  + " cal";
}

// adaugă masa în array-ul meals și reîncarcă Diary
function addMealToDiary() {
  const mealType = mealSelect.value;
  const foodId   = foodSelect.value;
  const grams    = parseFloat(gramsInput.value);

  if (!mealType || !foodId || isNaN(grams) || grams <= 0) {
    alert("Please select meal, food and enter a valid weight.");
    return;
  }

  // id simplu: 'm' + nr+1
  const newId = "m" + (meals.length + 1);
  meals.push({
    id: newId,
    date: "2025-05-21",      // fixezi azi sau preiei dintr-o variabilă
    mealType,
    foodId,
    grams
  });

  // reîncarci Diary și goli formularul
  loadDiaryForToday();
  mealSelect.value = "";
  foodSelect.value = "";
  gramsInput.value = "";
  updateDiaryMacros();
}

// legături evenimente
document.addEventListener("DOMContentLoaded", () => {
  populateFoodSelect();
  foodSelect.addEventListener("change", updateDiaryMacros);
  gramsInput.addEventListener("input", updateDiaryMacros);
  btnAddMeal.addEventListener("click", addMealToDiary);
});





function randomData(n, base, variation, decimals = 0) {
    return Array.from({ length: n }, () => {
      const v = base + (Math.random() * 2 - 1) * variation;
      return decimals > 0 ? +v.toFixed(decimals) : Math.round(v);
    });
}
  



let currentPeriod = 7;              // perioada curentă (în zile)
const periodButtons = document.querySelectorAll("#period-buttons button");
const periodInput   = document.getElementById("period-input");

function renderAnalyticsCharts(days = currentPeriod) {
    currentPeriod = days;
  
    // 1️⃣ Generăm array-ul de date ISO „YYYY-MM-DD”
    const today = new Date("2025-05-21");
    const dates = Array.from({ length: days }, (_, i) => {
      const d = new Date(today);
      d.setDate(d.getDate() - (days - 1 - i));
      return d;        // păstrăm obiect Date aici
    });
  
    // 2️⃣ Convertim în etichete simple „MM/DD”
    const labels = dates.map(d => {
      const mm = String(d.getMonth()+1).padStart(2,'0');
      const dd = String(d.getDate()).padStart(2,'0');
      return `${mm}/${dd}`;
    });
  
    // 3️⃣ Mock-up de date
    const caloriesData = randomData(days, 1800, 150);      // între 1650–1950 kcal
    const weightData   = randomData(days, 70, 0.5, 1);      // între 69.5–70.5 kg
    const stepsData    = randomData(days, 9000, 2000);      // între 7000–11000 pași
  
    // 4️⃣ Distrugem graficele vechi
    if (window.calChart) window.calChart.destroy();
    if (window.wtChart)  window.wtChart.destroy();
    if (window.stChart)  window.stChart.destroy();
  
    // 5️⃣ Creăm noile grafice cu etichete deja potrivite
    const commonOptions = {
      responsive: true,
      plugins: { legend:{ display:false } },
      scales: {
        x: {
          ticks: {
            autoSkip: true,
            maxRotation: 0,
            minRotation: 0
          }
        }
      }
    };
  
    // Calories
    window.calChart = new Chart(
      document.getElementById("caloriesChart").getContext("2d"), {
      type: "line",
      data: { labels, datasets:[{ data: caloriesData, fill:false, tension:0.4, borderWidth:2 }] },
      options: {
        ...commonOptions,
        scales: {
          ...commonOptions.scales,
          y: { beginAtZero:false }
        }
      }
    });
  
    // Weight
    window.wtChart = new Chart(
      document.getElementById("weightChart").getContext("2d"), {
      type: "line",
      data: { labels, datasets:[{ data: weightData, fill:false, tension:0.4, borderWidth:2 }] },
      options: {
        ...commonOptions,
        scales: {
          ...commonOptions.scales,
          y: { beginAtZero:false }
        }
      }
    });
  
    // Steps
    window.stChart = new Chart(
      document.getElementById("stepsChart").getContext("2d"), {
      type: "bar",
      data: { labels, datasets:[{ data: stepsData, borderWidth:1 }] },
      options: {
        ...commonOptions,
        scales: {
          ...commonOptions.scales,
          y: { beginAtZero:true }
        }
      }
    });
  }
  

// handler pentru click pe butoane
periodButtons.forEach(btn => {
  btn.addEventListener("click", () => {
    // marchează activ
    periodButtons.forEach(b => b.classList.remove("active"));
    btn.classList.add("active");

    const val = btn.dataset.days;
    if (val === "custom") {
      // arată input și golește-i valoarea
      periodInput.hidden = false;
      periodInput.value = "";
      periodInput.focus();
    } else {
      // perioadă numerică: ascunde input și rerandează chart-urile
      periodInput.hidden = true;
      renderAnalyticsCharts(parseInt(val, 10));
    }
  });
});

// când user-ul tastează în Custom
periodInput.addEventListener("keyup", e => {
  if (e.key === "Enter") {
    const x = parseInt(periodInput.value, 10);
    if (x > 0) {
      renderAnalyticsCharts(x);
    } else {
      alert("Enter a valid number of days");
    }
  }
});

// legăm și la tab click
buttons.analytics.addEventListener("click", () => {
  showSection("analytics");
  // dacă e pe Custom, păstrează inputul vizibil
  const active = document.querySelector("#period-buttons .active").dataset.days;
  periodInput.hidden = active !== "custom";
  renderAnalyticsCharts();
});

// la init
document.addEventListener("DOMContentLoaded", () => {
  renderAnalyticsCharts();
});

  
  // la click pe tab-ul Analytics:
  buttons.analytics.addEventListener("click", () => {
    showSection("analytics");
    renderAnalyticsCharts();
  });
  









  // calorii per minut pentru fiecare activitate
const calPerMin = {
    running: 10,
    swimming: 8,
    weightlifting: 6,
    cycling: 7,
    walking: 4
  };
  
  // istorice în memorie
  const exerciseHistory = [];
  const stepsHistory    = [];
  
  // referințe DOM
  const exType    = document.getElementById("exercise-type");
const exDur     = document.getElementById("exercise-duration");
const exCal     = document.getElementById("exercise-calories");
const btnCalcEx = document.getElementById("btn-calc-exercise");

  const exHistUl  = document.getElementById("exercise-history-list");
  
  const stepsIn   = document.getElementById("steps-input");
  const btnSaveSt = document.getElementById("btn-save-steps");
  const stHistUl  = document.getElementById("steps-history-list");
  
  // calculează și afișează calorii pe măsură ce se tastează
  function updateExerciseCalories() {
    const type = exType.value;
    const min  = parseFloat(exDur.value);
    if (type && !isNaN(min) && min > 0) {
      const cal = Math.round((calPerMin[type] || 0) * min);
      exCal.value = cal + " cal";
    } else {
      exCal.value = "";
    }
  }
  
  // adaugă în istoric exercițiu
  function addExerciseEntry() {
    const type = exType.value;
    const min  = parseFloat(exDur.value);
    const cal  = parseInt(exCal.value) || 0;
    if (!type || isNaN(min) || min <= 0) {
      console.log("DBG:", exType, exDur.value, exCal.value);
      alert("Select activity and enter valid duration.");
      return;
    }
    const timestamp = new Date().toLocaleTimeString();
    const entry = { id: Date.now().toString(), type, min, cal, timestamp };
    exerciseHistory.unshift(entry);
    renderExerciseHistory();
    // reset form
    exType.value = "";
    exDur.value  = "";
    exCal.value  = "";
  }

  exType.addEventListener("change", updateExerciseCalories);
  exDur.addEventListener("input", updateExerciseCalories);
  btnCalcEx.addEventListener("click",  addExerciseEntry);

  
  // afișează lista exerciții
  function renderExerciseHistory() {
    exHistUl.innerHTML = "";
    exerciseHistory.forEach(e => {
      const li = document.createElement("li");
      li.innerHTML = `
        <span>${e.timestamp} – ${e.type} for ${e.min} min → ${e.cal} cal</span>
        <span class="delete-exercise">✖</span>
      `;
      li.querySelector(".delete-exercise").addEventListener("click", () => {
        const idx = exerciseHistory.findIndex(x => x.id === e.id);
        exerciseHistory.splice(idx, 1);
        renderExerciseHistory();
      });
      exHistUl.appendChild(li);
    });
  }
  
  // pași
  function addStepsEntry() {
    const count = parseInt(stepsIn.value);
    if (isNaN(count) || count <= 0) {
      alert("Enter a valid number of steps.");
      return;
    }
    const timestamp = new Date().toLocaleTimeString();
    const entry = { id: Date.now().toString(), count, timestamp };
    stepsHistory.unshift(entry);
    renderStepsHistory();
    stepsIn.value = "";
  }
  
  function renderStepsHistory() {
    stHistUl.innerHTML = "";
    stepsHistory.forEach(s => {
      const li = document.createElement("li");
      li.innerHTML = `
        <span>${s.timestamp} – ${s.count} steps</span>
        <span class="delete-step">✖</span>
      `;
      li.querySelector(".delete-step").addEventListener("click", () => {
        const idx = stepsHistory.findIndex(x => x.id === s.id);
        stepsHistory.splice(idx, 1);
        renderStepsHistory();
      });
      stHistUl.appendChild(li);
    });
  }
  
  // evenimente
  exType.addEventListener("change", updateExerciseCalories);
  exDur.addEventListener("input", updateExerciseCalories);
  btnCalcEx.addEventListener("click", addExerciseEntry);
  
  btnSaveSt.addEventListener("click", addStepsEntry);

  

  // ==== Weight tracking ====

// ========== Weight records seed (21.04.2025 → 21.05.2025) ==========
const weightRecords = (() => {
    const records = [];
    const startDate   = new Date("2025-04-21");
    const endDate     = new Date("2025-05-21");
    const totalDays   = Math.round((endDate - startDate) / (1000*60*60*24));
    const startWeight = 73;
    const endWeight   = 70;
  
    for (let i = 0; i <= totalDays; i++) {
      const d = new Date(startDate);
      d.setDate(d.getDate() + i);
      const dateStr = d.toISOString().slice(0,10); // "YYYY-MM-DD"
      // scădere liniară
      const w = +(startWeight + (endWeight - startWeight) * (i/totalDays)).toFixed(1);
      records.push({ date: dateStr, weight: w });
    }
    return records;
  })();
  

// Salvează un nou record de greutate
document.getElementById("btn-save-weight").addEventListener("click", () => {
  const date = document.getElementById("weight-date").value;
  const w    = parseFloat(document.getElementById("weight-input").value);
  if (!date || isNaN(w)) {
    alert("Please select a date and enter a valid weight.");
    return;
  }
  weightRecords.push({ date, weight: w });
  updateWeightHistory();
  // opțional: golește input-urile
  document.getElementById("weight-input").value = "";
});

function updateWeightHistory() {
    const ul = document.getElementById("weight-history-list");
    ul.innerHTML = "";
  
    // sortăm descrescător după dată
    const sorted = weightRecords.slice().sort((a,b) => new Date(b.date) - new Date(a.date));
  
    sorted.forEach(record => {
      const li = document.createElement("li");
      li.textContent = `${record.date}: ${record.weight} kg`;
  
      // iconiţa de trash
      const del = document.createElement("i");
      del.className = "fa-solid fa-trash delete-weight";
      del.title = "Delete";
      li.appendChild(del);
  
      // handler-ul de click
      del.addEventListener("click", () => {
        if (!confirm(`Ștergi înregistrarea de ${record.weight} kg din ${record.date}?`)) return;
        // găsim index-ul în array-ul original și ştergem
        const idx = weightRecords.indexOf(record);
        if (idx > -1) weightRecords.splice(idx, 1);
        updateWeightHistory(); // reîmprospătăm lista
      });
  
      ul.appendChild(li);
    });
  }
  

// La inițializare
document.addEventListener("DOMContentLoaded", () => {
  updateWeightHistory();
  updateMainPage();
});


// 1. Toggle theme
document.getElementById("btn-toggle-theme").addEventListener("click", () => {
    settings.theme = settings.theme === "light" ? "dark" : "light";
    localStorage.setItem(SETTINGS_KEY, JSON.stringify(settings));
    applyTheme();
  });
  
  // 2. Save calorie goal
  document.getElementById("btn-save-calorie-goal").addEventListener("click", () => {
    const v = parseInt(document.getElementById("input-calorie-goal").value, 10);
    if (isNaN(v) || v <= 0) return alert("Introdu o valoare validă!");
    settings.calorieGoal = v;
    localStorage.setItem(SETTINGS_KEY, JSON.stringify(settings));
    alert("Calorie goal salvat: " + v);
    loadDiaryForToday(); // pentru a reîmprospăta formula
  });
  
  // 3. Save steps goal
  document.getElementById("btn-save-steps-goal").addEventListener("click", () => {
    const v = parseInt(document.getElementById("input-steps-goal").value, 10);
    if (isNaN(v) || v <= 0) return alert("Introdu o valoare validă!");
    settings.stepsGoal = v;
    localStorage.setItem(SETTINGS_KEY, JSON.stringify(settings));
    alert("Steps goal salvat: " + v);
    // poți apela funcția care afișează Steps, dacă vrei să le marchezi diferit
  });
  
  // 4. Reset all data
  document.getElementById("btn-reset-all").addEventListener("click", () => {
    if (!confirm("Ștergi toate datele (meals, foods, settings)?")) return;
    localStorage.clear();
    location.reload();
  });
  

  function updateMainPage() {
    const today = "2025-05-21"; // or pull from a date picker
    
    // 1) Meals & Food
    const mealsToday = meals.filter(m => m.date === today);
    const foodMap    = Object.fromEntries(foods.map(f => [f.id, f]));
  
    let totalFoodCal = 0;
    let totalProt    = 0;
    let totalCarb    = 0;
    let totalFat     = 0;
  
    mealsToday.forEach(m => {
      const f = foodMap[m.foodId];
      if (!f) return;
      const factor = m.grams / f.weight;
      totalFoodCal += f.calories * factor;
      totalProt     += f.protein  * factor;
      totalCarb     += f.carbs    * factor;
      totalFat      += f.fats     * factor;
    });
  
    // 2) Exercise (use your exerciseHistory array)
      const exToday   = exerciseHistory;          // all entries are for “today”
      let totalExCal  = 0;
      let totalExMin  = 0;

      exToday.forEach(e => {
        totalExMin += e.min;                      // your code pushes `{ min, cal, … }`
      totalExCal += e.cal;
    });
  
    // 3) Steps
    const stepsToday = stepsHistory;   
    const totalSteps = stepsToday.reduce((sum, s) => sum + s.count, 0);
  
    // 4) Goals
    const calGoal   = settings.calorieGoal;
    const stepsGoal = settings.stepsGoal;
  
    // 5) Calories card
    document.getElementById("base-goal").innerText              = calGoal;
    document.getElementById("calories-from-food").innerText     = Math.round(totalFoodCal);
    document.getElementById("calories-from-exercise").innerText = Math.round(totalExCal);
    document.getElementById("calories-remaining").innerText     = Math.round(calGoal - totalFoodCal + totalExCal);
  
    // 6) Macros
    const protGoal = Math.round((calGoal * 0.3) / 4);
    const carbGoal = Math.round((calGoal * 0.4) / 4);
    const fatGoal  = Math.round((calGoal * 0.3) / 9);
  
    document.getElementById("macro-protein-current").innerText = Math.round(totalProt);
    document.getElementById("macro-protein-goal").innerText    = protGoal;
  
    document.getElementById("macro-carbs-current").innerText   = Math.round(totalCarb);
    document.getElementById("macro-carbs-goal").innerText      = carbGoal;
  
    document.getElementById("macro-fats-current").innerText    = Math.round(totalFat);
    document.getElementById("macro-fats-goal").innerText       = fatGoal;
  
    // 7) Steps card
    document.getElementById("steps-current").innerText = totalSteps;
    document.getElementById("steps-goal").innerText    = stepsGoal;
  
    // 8) Exercise card
    document.getElementById("exercise-calories").innerText = Math.round(totalExCal);
    const h = Math.floor(totalExMin / 60);
    const m = totalExMin % 60;
    document.getElementById("exercise-duration").innerText = `${h}:${String(m).padStart(2,'0')} hr`;
  }

  
  document.addEventListener("DOMContentLoaded", () => {
    showSection("main");
    updateMainPage();
  });
  