import { StrictMode } from "react";
import { createRoot }from "react-dom/client";
import App from "./App";
import { AuthProvider } from "react-oidc-context";

const cognitoAuthConfig = {
    authority: "https://cognito-idp.us-east-2.amazonaws.com/us-east-2_NyEcKzFV5",
    client_id: "7ou4s0s3nbaprr9q8eh9kh06h2",
    redirect_uri: "https://d84l1y8p4kdic.cloudfront.net",
    response_type: "code",
    scope: "phone openid email",
};

const root = createRoot(document.getElementById("root")!);

root.render(
    <StrictMode>
        <AuthProvider {...cognitoAuthConfig}>
            <App />
        </AuthProvider>
    </StrictMode>
);