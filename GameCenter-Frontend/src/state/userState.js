import { reactive } from "vue";

export const userState = reactive({
    userInfo: (() => {
        const savedUser = localStorage.getItem("userInfo");
        if (savedUser) {
            const parsedUser = JSON.parse(savedUser);
            console.log('Loaded user from storage:', parsedUser); // Debug log
            return parsedUser;
        }
        return null;
    })(),

    setUser(user) {
        console.log('Setting user:', user); // Debug log
        this.userInfo = {
            ...user,
            userType: user.userType || 'Client' // Ensure userType is set
        };
        localStorage.setItem("userInfo", JSON.stringify(this.userInfo));
    },

    clearUser() {
        this.userInfo = null;
        localStorage.removeItem("userInfo");
    },
});

// Debug log when the module loads
console.log('Initial userState:', userState.userInfo);