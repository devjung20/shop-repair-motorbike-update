import jwtDecode from "jwt-decode";

const JWT_STORAGE_KEY = "jwt_storage_key";

export const getJwtToken = () => {
  const token = localStorage.getItem(JWT_STORAGE_KEY);

  try {
    const { exp } = jwtDecode(token);
    const isTokenExpired = exp * 1000 < Date.now();

    if (isTokenExpired) {
      removeJwtToken(token);
      return null;
    }

    return token;

    // Handle error
  } catch (error) {
    return null;
  }
};

export const setJwtToken = (token) => {
  localStorage.setItem(JWT_STORAGE_KEY, token);
};

export const removeJwtToken = () => {
  localStorage.removeItem(JWT_STORAGE_KEY);
};

export const getJwtData = () => {
  const token = getJwtToken();

  try {
    const { email, username, role, id } = jwtDecode(token);
    return { email, username, role, id };

    // Handle error
  } catch (error) {
    return {};
  }
};
