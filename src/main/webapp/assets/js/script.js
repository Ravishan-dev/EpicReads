(function () {
    "use strict";

    function initNavSearchToggle() {
        const toggleBtn = document.getElementById("searchToggle");
        const closeBtn = document.getElementById("searchClose");
        const wrap = document.getElementById("navSearch");
        const input = document.getElementById("searchInput");

        if (!toggleBtn || !closeBtn || !wrap || !input) return;

        function openSearch() {
            wrap.classList.remove("d-none");
            toggleBtn.setAttribute("aria-expanded", "true");
            setTimeout(() => input.focus(), 0);
        }

        function closeSearch() {
            wrap.classList.add("d-none");
            toggleBtn.setAttribute("aria-expanded", "false");
            input.value = "";
            toggleBtn.focus();
        }

        toggleBtn.addEventListener("click", () => {
            const isOpen = !wrap.classList.contains("d-none");
            if (isOpen) closeSearch();
            else openSearch();
        });

        closeBtn.addEventListener("click", closeSearch);

        document.addEventListener("keydown", (e) => {
            if (e.key === "Escape" && !wrap.classList.contains("d-none")) closeSearch();
        });

        document.addEventListener("click", (e) => {
            if (wrap.classList.contains("d-none")) return;
            const clickedInside = wrap.contains(e.target) || toggleBtn.contains(e.target);
            if (!clickedInside) closeSearch();
        });
    }

    // function initAccountLink() {
    //     const link = document.getElementById("accountLink");
    //     if (!link) return;
    //
    //     // UI-only auth toggle (replace with real backend logic later)
    //     const authToken = localStorage.getItem("authToken");
    //
    //     if (authToken) {
    //         link.href = "my-account.html";
    //         link.setAttribute("aria-label", "My account");
    //     } else {
    //         link.href = "login.html";
    //         link.setAttribute("aria-label", "Login");
    //     }
    // }

    function initPasswordToggles() {
        // Supports multiple toggles if you ever add more
        const buttons = document.querySelectorAll('[data-toggle="password"]');

        buttons.forEach((btn) => {
            const inputId = btn.getAttribute("data-target");
            const input = inputId ? document.getElementById(inputId) : null;
            if (!input) return;

            btn.addEventListener("click", () => {
                const isPw = input.type === "password";
                input.type = isPw ? "text" : "password";
                btn.innerHTML = isPw
                    ? '<i class="bi bi-eye-slash"></i>'
                    : '<i class="bi bi-eye"></i>';
                btn.setAttribute("aria-label", isPw ? "Hide password" : "Show password");
            });
        });
    }

    // Load header and footer components
    function loadComponents() {
        // Load header
        fetch('./header.html')
            .then(response => response.text())
            .then(html => {
                const headerContainer = document.getElementById('headerContainer');
                if (headerContainer) {
                    headerContainer.innerHTML = html;
                    // Re-initialize scripts that depend on header elements
                    initNavSearchToggle();
                }
            })
            .catch(error => console.error('Error loading header:', error));

        // Load footer
        fetch('./footer.html')
            .then(response => response.text())
            .then(html => {
                const footerContainer = document.getElementById('footerContainer');
                if (footerContainer) {
                    footerContainer.innerHTML = html;
                }
            })
            .catch(error => console.error('Error loading footer:', error));
    }

    // Load components when DOM is ready
    if (document.readyState === 'loading') {
        document.addEventListener("DOMContentLoaded", () => {
            loadComponents();
            initNavSearchToggle();
            initPasswordToggles();
        });
    } else {
        // DOM is already loaded
        loadComponents();
        initNavSearchToggle();
        initPasswordToggles();
    }
})();