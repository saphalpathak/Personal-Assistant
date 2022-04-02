let weather = {
    fetchWeather: function () {
        fetch(
            "https://api.openweathermap.org/data/2.5/weather?q=kathmandu&units=metric&appid=a7b17f23a1783603b7525a6d223d4afc"
        )
            .then((response) => {
                return response.json();
            })
            .then((data) => this.displayWeather(data));
    },
    displayWeather: function (data) {
        const { icon } = data.weather[0];
        const { temp, humidity } = data.main;
        const { speed } = data.wind;
        document.querySelector(".city").innerText = "Kathmandu";
        document.querySelector(".icon").src =
            "https://openweathermap.org/img/wn/" + icon + ".png";
        document.querySelector(".temp").innerText = temp + "°C";
        document.querySelector(".humidity").innerText =
            "Humidity: " + humidity + "%";
        document.querySelector(".wind").innerText =
            "Wind speed: " + speed + " km/h";
    }
};
weather.fetchWeather();