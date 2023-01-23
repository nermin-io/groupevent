import type { AppProps } from "next/app";
import Layout from "../components/layout/Layout";
import { globalStyles } from "../common/global";
import { QueryClient, QueryClientProvider } from "react-query";

export default function App({ Component, pageProps }: AppProps) {
  globalStyles();
  const queryClient = new QueryClient();
  
  return (
    <QueryClientProvider client={queryClient}>
      <Layout>
        <Component {...pageProps} />
      </Layout>
    </QueryClientProvider>
  );
}
