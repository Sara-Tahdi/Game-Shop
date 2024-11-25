import { reactive } from "vue";

export const userState = reactive({
    userInfo: JSON.parse(localStorage.getItem("userInfo")) || null,
    setUser(user) {
        this.userInfo = user;
        localStorage.setItem("userInfo", JSON.stringify(user));
    },
    clearUser() {
        this.userInfo = null;
        localStorage.removeItem("userInfo");
    },
});