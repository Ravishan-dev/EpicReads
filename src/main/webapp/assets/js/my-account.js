window.addEventListener("load", async () => {
    Notiflix.Loading.pulse("Please Wait...", {
        clickToClose: false,
        svgColor: '#0248c7',
    });

    try {
        await loadCities();
        await loadUser();
    } finally {
        Notiflix.Loading.remove(1000);
    }
});

async function updateProfile() {
    let fName = document.getElementById("firstName");
    let lName = document.getElementById("lastName");
    let email = document.getElementById("email");
    let mobile = document.getElementById("phone");
    let line1 = document.getElementById("lineOne");
    let line2 = document.getElementById("lineTwo");
    let city = document.getElementById("accountCity");
    let pcode = document.getElementById("postalCode");

    // Debug logging
    console.log("City Select Value:", city.value);
    console.log("City Select Type:", typeof city.value);

    const cityId = parseInt(city.value);
    console.log("Parsed City ID:", cityId);

    if (cityId === 0 || isNaN(cityId)) {
        Notiflix.Notify.failure("Please Select A City", {
            position: 'center-top'
        });
        return;
    }

    const user = {
        firstName: fName.value,
        lastName: lName.value,
        email: email.value,
        mobile: mobile.value,
        lineOne: line1.value,
        lineTwo: line2.value,
        cityId: cityId,
        postalCode: pcode.value
    };

    console.log("Sending user object:", user);

    try {
        const response = await fetch("api/profiles/update-profile", {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        });

        const data = await response.json();
        console.log("Response:", data);

        if (response.ok) {
            if (data.status) {
                Notiflix.Notify.success(data.message, {
                    position: 'center-top'
                });
            } else {
                Notiflix.Notify.failure(data.message, {
                    position: 'center-top'
                });
            }
        } else {
            Notiflix.Notify.failure(data.message || "Profile Update Failed", {
                position: 'center-top'
            });
        }
    } catch (e) {
        console.error("Error:", e);
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

async function loadCities() {

    try {
        const response = await fetch("api/data/cities");
        if (response.ok) {
            const data = await response.json();
            const citySelect = document.getElementById("accountCity");
            data.cities.forEach((city) => {
                const option = document.createElement("option");
                option.value = city.id;  // Make sure it's an integer
                option.textContent = city.name;
                citySelect.appendChild(option);
            });
        } else {
            Notiflix.Notify.failure("City Loading Failed", {
                position: 'center-top'
            });
        }
    } catch (e) {
        console.error("Error loading cities:", e);
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

async function loadUser() {

    try {
        const response = await fetch("api/profiles/user-profile", {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
        });

        if (response.ok) {
            if (response.redirected) {
                window.location = response.url;
                return;
            } else {
                const data = await response.json();

                document.getElementById("firstName").value = data.user.firstName;
                document.getElementById("lastName").value = data.user.lastName;
                document.getElementById("email").value = data.user.email;
                document.getElementById("phone").value = data.user.mobile;
                document.getElementById("lineOne").value = data.user.lineOne;
                document.getElementById("lineTwo").value = data.user.lineTwo;
                document.getElementById("accountCity").value = data.user.cityId;
                document.getElementById("postalCode").value = data.user.postalCode;

                console.log("Set city value to:", data.user.cityId);
            }
        } else {
            Notiflix.Notify.failure("Profile Loading Failed", {
                position: 'center-top'
            });
        }
    } catch (e) {
        console.error("Error loading user:", e);
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}