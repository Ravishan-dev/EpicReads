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

async function loadCities() {

    try {
        const response = await fetch("api/data/cities");
        if (response.ok) {
            const data = await response.json();
            const citySelect = document.getElementById("accountCity");
            data.cities.forEach((city) => {
                const option = document.createElement("option");
                option.value = city.id;
                option.innerHTML = city.name;
                citySelect.appendChild(option);
            });
        } else {
            Notiflix.Notify.failure("City Loading Failed", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }
}

async function loadUser() {

    const fname = document.getElementById("firstName");
    const lname = document.getElementById("lastName");
    const email = document.getElementById("email");
    const mobile = document.getElementById("phone");
    const line1 = document.getElementById("lineOne");
    const line2 = document.getElementById("lineTwo");
    const city = document.getElementById("accountCity");
    const pcode = document.getElementById("postalCode");

    const user = {
        firstName: fname.value,
        lastName: lname.value,
        email: email.value,
        mobile: mobile.value,
        lineOne: line1.value,
        lineTwo: line2.value,
        city: city.value,
        postalCode: pcode.value
    }

    try {
        const response = await fetch("api/profiles/user-profile", {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify(user)
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

            }
        } else {
            Notiflix.Notify.failure("Profile Loading Failed", {
                position: 'center-top'
            });
        }
    } catch (e) {
        Notiflix.Notify.failure(e.message, {
            position: 'center-top'
        });
    }

}