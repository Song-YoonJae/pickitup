"use client"
import type { Metadata } from "next";
import { usePathname } from "next/navigation"
import { Inter } from "next/font/google";
import "./globals.css";
import Image from "next/image";
import Link from "next/link";
import { IoLogoGitlab } from "react-icons/io5";

interface LinkType {
  name: string;
  href: string;
}

const inter = Inter({ subsets: ["latin"] });
const navLinks: LinkType[] = [
  { name: "채용공고", href: "/recruit",},
  { name: "기술블로그", href: "/techBlog" },
  { name: "미니 게임", href: "/game" },
  { name: "면접 대비", href: "/interview" },
];

// export const metadata: Metadata = {
//   title: "pick IT up",
//   description: "pick your IT information and recruit",
// };

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const pathname = usePathname();
  return (
    <html lang="kr">
      <body className={`${inter.className} min-h-screen flex flex-col`}>
        <header className="flex justify-between">
          <div>
            <Link href="/">
              <Image src="/pickITup.svg" alt="logo" width="200" height="1" />
            </Link>
          </div>
          <div className="flex text-center justify-center">
            {navLinks.map((link: LinkType) => {
              const isActive = pathname.startsWith(link.href);
              return (
                <Link
                  href={link.href}
                  key={link.name}
                  className={isActive ? "font-bold mr-4" :  "text-blue-500 mr-4"}
                >
                  {link.name}
                </Link>
              );
            })}
          </div>
          <div>로그인 | 회원가입</div>
        </header>
        <main className="flex-grow">{children}</main>
        <footer className="mt-auto flex justify-evenly items-center bg-gray-400 text-white text-xs h-16">
          <div className="w-1/12"></div>
          <div>Copyright © 2024. SSAFY 특화 A_406 F5 세희고침</div>
          <div className="w-3/12"></div>
          <div>서비스 이용약관</div>
          <div>개인 정보 처리방침</div>
          <div><IoLogoGitlab className="inline-block"/> Gitlab</div>
          <div className="w-1/12"></div>
        </footer>
      </body>
    </html>
  );
}
