import { globalCss } from "../stitches.config";
import { inter } from "./fonts";

export const globalStyles = globalCss({
    '*': { boxSizing: 'border-box' },
    'a': {
        color: 'inherit',
        textDecoration: 'none'
    },
    'body': {
        backgroundColor: '#F9F9F9'
    },
    'html, body': {
        padding: 0,
        margin: 0,
        fontSize: '16px',
        ...inter.style
    },
    'h1': {
        all: 'unset',
        fontSize: '4.209rem'
    },
    'h2': {
        all: 'unset',
        fontSize: '3.157rem'
    },
    'h3': {
        all: 'unset',
        fontSize: '2.369rem'
    },
    'h4': {
        all: 'unset',
        fontSize: '1.666rem'
    },
    'h5': {
        all: 'unset',
        fontSize: '1.333rem'
    },
    'h6': {
        all: 'unset',
        fontSize: '1rem'
    }
});
