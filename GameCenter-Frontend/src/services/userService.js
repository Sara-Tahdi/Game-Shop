export const userService = {
  saveUserType(userType) {
    localStorage.setItem("userType", userType);
  },
  getUserType() {
    return localStorage.getItem("userType");
  },
  saveUserDetails(userDetails) {
    localStorage.setItem("userDetails", JSON.stringify(userDetails));
  },
  getUserDetails() {
    const details = localStorage.getItem("userDetails");
    return details ? JSON.parse(details) : null;
  },
  userLoggedIn() {
    return localStorage.userDetails !== null;
  },
  clearUser() {
    localStorage.clear();
  },
};
