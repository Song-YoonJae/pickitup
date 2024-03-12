import Image from 'next/image'
import Link from 'next/link'
export default function Login(){
    return (
      <div className="flex flex-col justify-center items-center w-full h-[70vh]">

        <div className="flex items-center justify-center h-[10vh] text-3xl font-bold">pick IT up 로그인</div>
        <div className="h-[30vh] w-[36vw] h-[34vh] rounded-[10px] border border-[#d9d9d9]">
          
          <form action="#" method="POST">
            <div className='flex w-full h-[6vh] justify-center items-center mt-14'>
              <label htmlFor="id" className="text-sm text-black text-lg font-bold w-[10vh]">아이디</label>
              <input placeholder="아이디를 입력하세요" className="w-[16vw] ml-6 rounded-md bg-[#f5f5f5] border border-[#d9d9d9]"/>
            </div>

            <div className='flex w-full h-[6vh] justify-center items-center'>
              <label htmlFor="password" className='w-[10vh] text-lg font-bold'>비밀번호</label>
              <input placeholder="비밀번호를 입력하세요" type="password" className="w-[16vw] ml-6 rounded-md bg-[#f5f5f5] border border-[#d9d9d9]"/>
            </div>

            <div className="flex w-full h-[14vh] justify-center items-center">
              <button type="submit" className="w-[18vw] h-[5vh] rounded-md bg-[#00ce7c] text-white text-lg font-bold">로그인</button>
            </div>
          </form>

        </div>
        <div className="flex items-center justify-center h-[8vh] text-lg whitespace-pre">아직 계정이 없으신가요?  <Link href="/membership" className="text-lg font-bold">회원가입 하러가기</Link></div>
      </div>
    )
}