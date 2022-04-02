let weather = {
    fetchWeather: function (lat,lon) {
        fetch(
            "https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=a7b17f23a1783603b7525a6d223d4afc"
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
        document.querySelector(".city").innerText = data.name;
        document.querySelector(".icon").src =
            "https://openweathermap.org/img/wn/" + icon + ".png";
        //api return kelvin so -273.15 to convert kelvin into celsius (.toLocaleString for ony 2 digit available after decimal)
        document.querySelector(".temp").innerText = (temp-273).toLocaleString(undefined, { maximumFractionDigits: 2, minimumFractionDigits: 2 }) + "°C";
        document.querySelector(".humidity").innerText =
            "Humidity: " + humidity + "%";
        document.querySelector(".wind").innerText =
            "Wind speed: " + speed + " km/h";
    }
};
navigator.geolocation.getCurrentPosition(function (position){
    let lat = position.coords.latitude;
    let lon = position.coords.longitude;
    weather.fetchWeather(lat,lon);
})