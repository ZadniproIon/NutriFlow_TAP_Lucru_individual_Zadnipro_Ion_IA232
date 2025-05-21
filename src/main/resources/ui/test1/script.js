
/**
 * Simple client‑side router for NutriFlow single‑page app
 * Toggles visibility of <section.page> elements based on sidebar clicks
 */
(function () {
  const navItems = document.querySelectorAll('.nav-item');
  const pages = document.querySelectorAll('.page');
  const todayHeading = document.getElementById('today-heading');

  /* === Helpers === */
  const showPage = (id) => {
    pages.forEach(p => {
      if (p.id === id) {
        p.classList.add('active');
      } else {
        p.classList.remove('active');
      }
    });
  };

  /* === Navigation click events === */
  navItems.forEach(item => {
    item.addEventListener('click', () => {
      // highlight nav
      navItems.forEach(n => n.classList.remove('active'));
      item.classList.add('active');

      // show page
      const target = item.dataset.target;
      showPage(target);
    });
  });

  /* === Default state === */
  showPage('main');

  /* === Set today's date === */
  const now = new Date();
  const formatted = now.toLocaleDateString('en-GB', {
    day: 'numeric',
    month: 'long',
    year: 'numeric'
  });
  todayHeading.textContent = `Today: ${formatted}`;
})();
