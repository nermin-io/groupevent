import type { AppProps as NextAppProps } from "next/app";
import Layout from "@/components/layout/Layout";
import { globalStyles } from "@/common/global";
import { QueryClient, QueryClientProvider } from "react-query";
import AuthContextProvider from "@/providers/AuthContextProvider";
import LocalStorageProvider from "@/providers/LocalStorageProvider";

type AppProps<P = any> = {
  pageProps: P;
} & Omit<NextAppProps<P>, "pageProps">;

export default function App({ Component, pageProps }: AppProps) {
  globalStyles();
  const queryClient = new QueryClient();

  const AuthenticatedComponent = () => (
    <AuthContextProvider session={pageProps.session}>
      <Component {...pageProps} />
    </AuthContextProvider>
  );

  return (
    <QueryClientProvider client={queryClient}>
      <LocalStorageProvider>
        <Layout>
          {pageProps.session ? (
            <AuthenticatedComponent />
          ) : (
            <Component {...pageProps} />
          )}
        </Layout>
      </LocalStorageProvider>
    </QueryClientProvider>
  );
}
