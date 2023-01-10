import type {AppProps} from 'next/app';
import Layout from "../components/layout/Layout";
import {globalStyles} from "../common/global";

export default function App({Component, pageProps}: AppProps) {
    globalStyles();
    return (
        <Layout>
            <Component {...pageProps} />
        </Layout>
    )
}
